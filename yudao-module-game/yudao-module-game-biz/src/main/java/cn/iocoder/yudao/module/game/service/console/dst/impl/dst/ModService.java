package cn.iocoder.yudao.module.game.service.console.dst.impl.dst;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.dstmods.DstModsDO;
import cn.iocoder.yudao.module.game.dal.mysql.dstmods.DstModsMapper;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.SteamModInfo;
import cn.iocoder.yudao.module.game.util.SteamUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ModService {

    @Autowired
    private DstModsMapper dstModDao;

    Semaphore produceSemaphore = new Semaphore(5);
    Semaphore consumeSemaphore = new Semaphore(10);
    private ReentrantLock lock = new ReentrantLock();
    private Condition empty = lock.newCondition();
    private Condition full = lock.newCondition();

//    List<Long> notExistModIds = new CopyOnWriteArrayList<>();

    private Queue<Long> notExistModIds = new ConcurrentLinkedQueue();
    Integer totalPage = 466;
    AtomicInteger currentPageIndex = new AtomicInteger(400);

    public void workshopSpiderProduce() {
        try {
            produceSemaphore.acquire();
            lock.lock();
            while (true) {
                if (currentPageIndex.get() > totalPage) {
                    log.info(Thread.currentThread().getName() + "到达最后一页");
                    break;
                }
                List<String> modIds = SteamUtil.dstWorkshopPaginationParser(currentPageIndex.getAndIncrement()); // 取值后+1
                if (modIds.size() == 0) {
                    log.info(Thread.currentThread().getName() + "获取到模组列表为空");
                    break;
                }
                List<Long> modIdsValue = new ArrayList<>();
                for (String modId : modIds) {
                    modIdsValue.add(Long.parseLong(modId));
                }
                List<DstModsDO> mbtDstModEntities = dstModDao.selectList(new LambdaQueryWrapperX<DstModsDO>().inIfPresent(DstModsDO::getModId, modIdsValue));

                List<Long> existModIds = mbtDstModEntities.stream().map(e -> e.getModId()).collect(Collectors.toList());

                boolean modNeedAdd = false;
                for (Long modId : modIdsValue) {
                    if (existModIds.contains(modId)) {
                        continue;
                    }
                    notExistModIds.add(modId);
                    log.info(Thread.currentThread().getName() + "生产者:{}, notExistModIds.size()={}", modId, notExistModIds.size());
                    modNeedAdd = true;
                }
                if (modNeedAdd) {
                    empty.signalAll();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            produceSemaphore.release();
        }

    }

    public void workshopSpiderConsume() {
        try {
            consumeSemaphore.acquire();
            lock.lock();
            while (true) {
                if (notExistModIds.size() == 0) {
                    try {
                        empty.await(1, TimeUnit.MINUTES);// 超过1分钟无数据, 自动停止
                    } catch (InterruptedException e) {
                        log.warn(Thread.currentThread().getName() + "已超过1分钟无数据产生, 自动退出模组爬取消费端");
                        throw new RuntimeException(e);
                    }
                }
                if (notExistModIds.size() > 0) {
                    Long modId = notExistModIds.poll();
                    log.info(Thread.currentThread().getName() + "消费者:{}", modId);
//                    updateDbMod(String.valueOf(modId));
                    full.signalAll();
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            consumeSemaphore.release();

        }
    }

    public DstModsDO modInfoToEntity(SteamModInfo modInfo, DstModsDO modEntity) {
        modEntity.setModId(Long.parseLong(modInfo.getModId()));
        modEntity.setModTitle(modInfo.getTitle());
        modEntity.setIsServerMod(modInfo.getIsServerMod());
        modEntity.setUrl(modInfo.getUrl());
        modEntity.setTags(modInfo.getTags());
        modEntity.setImage(modInfo.getImage());
        modEntity.setStatus(modInfo.getStatus());
        modEntity.setAuthor(modInfo.getAuthor());
        modEntity.setVersion(modInfo.getVersion());
        modEntity.setFileSize(modInfo.getFilesize());
        modEntity.setModPublishTime(modInfo.getModPublishTime());
        modEntity.setModUpdateTime(modInfo.getModUpdateTime());
        modEntity.setStarUrl(modInfo.getStarUrl());
        if (modInfo.getDependMods() != null && modInfo.getDependMods().size() > 0) {
            String dependMods = JSON.toJSONString(modInfo.getDependMods());
            modEntity.setDependMods(dependMods);
        }
        return modEntity;
    }

    public SteamModInfo updateDbMod(String modId) {
        SteamModInfo modInfo = SteamUtil.getModInfo(modId);
        DstModsDO dstModEntity = dstModDao.selectOne(new LambdaQueryWrapperX<DstModsDO>().eq(DstModsDO::getModId, Long.parseLong(modId)));
        try {
            if (dstModEntity == null) {
                dstModEntity = new DstModsDO();
                modInfoToEntity(modInfo, dstModEntity);
                dstModDao.insertOrUpdate(dstModEntity);
                log.info("mod:{}已插入", modId);
            } else {
                if (StringUtils.isNotEmpty(modInfo.getModUpdateTime()) && !modInfo.getModUpdateTime().equals(dstModEntity.getModUpdateTime())) {
                    modInfoToEntity(modInfo, dstModEntity);
                    dstModDao.updateById(dstModEntity);
                    log.info("mod:{}已更新, 最新更新时间:{}", modId, modInfo.getModUpdateTime());
                } else {
                    log.info("mod:{}无需更新", modId);
                }
            }
        } catch (Exception exception) {
            log.error("modId:{}保存DB遇到错误, " + ExceptionUtils.getStackTrace(exception), modId);
        }
        return modInfo;
    }


}


class Produce implements Runnable {

    ModService modSpider;

    Produce(ModService modSpider) {
        this.modSpider = modSpider;
    }

    @Override
    public void run() {
        modSpider.workshopSpiderProduce();
    }
}

class Consumer implements Runnable {

    ModService modSpider;

    Consumer(ModService modSpider) {
        this.modSpider = modSpider;
    }

    @Override
    public void run() {
        modSpider.workshopSpiderConsume();
    }
}
