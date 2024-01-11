package cn.iocoder.yudao.module.game.service.console.dst.timer;

import cn.hutool.core.date.StopWatch;
import com.dooyo.dao.MBTDstPlayerDao;
import com.dooyo.dao.MBTInstanceDao;
import com.dooyo.dao.MBTUserRentDao;
import com.dooyo.dao.entity.MBTDstPlayerEntity;
import com.dooyo.dao.entity.MBTDstPlayerEntityExample;
import com.dooyo.dao.entity.MBTInstanceEntity;
import com.dooyo.dao.entity.MBTUserRentEntity;
import com.dooyo.dao.entity.MBTUserRentEntityExample;
import com.dooyo.dao.entity.MBTUserRentEntityExample.Criteria;
import com.dooyo.service.clients.dst.DstClient;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.Player;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.WorldInfo;
import cn.iocoder.yudao.module.game.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// 检测游戏内玩家数量
@Slf4j
@Service
public class PlayerDetectService {

    @Autowired
    MBTInstanceDao instanceDao;

    @Autowired
    MBTUserRentDao userRentDao;

    @Autowired
    MBTDstPlayerDao dstPlayerDao;

    ExecutorService executor = new ThreadPoolExecutor(5, 5, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());


    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("定时检测局内玩家人数");

        MBTUserRentEntityExample example = new MBTUserRentEntityExample();
        Criteria criteria = example.createCriteria();
        criteria.andEndTimeGreaterThan(new Date());
        criteria.andSaleEqualTo(true);
        criteria.andGameCodeEqualTo("dst");
        List<MBTUserRentEntity> mbtUserRentEntities = userRentDao.selectByExample(example);
        DstClient client = DstClient.getInstance();
        CountDownLatch countDownLatch = new CountDownLatch(mbtUserRentEntities.size());
        for (MBTUserRentEntity mbtUserRentEntity : mbtUserRentEntities) {
            executor.submit(() -> {
                Long rentId = mbtUserRentEntity.getId();
                Long userId = mbtUserRentEntity.getUserId();
                Long instanceId = mbtUserRentEntity.getInstanceId();
                String ip = mbtUserRentEntity.getInstanceIp();
                MBTInstanceEntity instance = instanceDao.selectByPrimaryKey(instanceId);
                List<Player> onlinePlayers = client.getOnlinePlayers(instance);
                int number = onlinePlayers.size();
                try {
                    if (number == 0) {
                        // 判断前一次是不是玩家人数大于0，说明玩家在刚刚的时候全部退出游戏了.
                        MBTDstPlayerEntityExample dstPlayerEntityExample = new MBTDstPlayerEntityExample();
                        dstPlayerEntityExample.setOrderByClause("create_time desc");
                        dstPlayerEntityExample.createCriteria().andRentIdEqualTo(rentId).andUserIdEqualTo(userId);
                        MBTDstPlayerEntity mbtDstPlayerEntity = dstPlayerDao.selectOneByExample(dstPlayerEntityExample);
                        if (mbtDstPlayerEntity.getNumber() != null && mbtDstPlayerEntity.getNumber() > 0) {
                            // 说明刚刚玩家全部退出
                            String fileName = "";
                            try {
                                WorldInfo worldInfo = client.getWorldInfo(instance);
                                int cycles = worldInfo.getClock().getCycles();
                                String nowDateTime = DateFormatUtil.formatDateTime(new Date(), DateFormatUtil.Pattern._41);
                                fileName = "存档备份__租约ID" + rentId + "__世界第" + (cycles + 1) + "天__" + nowDateTime + ".zip";
                            } catch (Exception exception) {
                                fileName = "";
                            }
                            log.info("租约ID:{}, 根据玩家退出情况创建存档备份:{}", rentId, fileName);
                            client.zipArchive(instance, fileName);
                        }
                    }

                } catch (Exception exception) {
                    log.error(exception.getMessage());
                } finally {
                    MBTDstPlayerEntity record = new MBTDstPlayerEntity();
                    record.setUserId(userId);
                    record.setRentId(rentId);
                    record.setInstanceIp(ip);
                    record.setNumber(number);
                    dstPlayerDao.insertSelective(record);
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("定时检测局内玩家人数, 耗时:" + stopWatch.getTotalTimeSeconds() + "秒");
    }

}
