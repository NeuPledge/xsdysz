package cn.iocoder.yudao.module.game.service.console.dst.timer;

import cn.hutool.core.date.StopWatch;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.dal.dataobject.player.PlayerDO;
import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;
import cn.iocoder.yudao.module.game.dal.mysql.instance.InstanceMapper;
import cn.iocoder.yudao.module.game.dal.mysql.player.PlayerMapper;
import cn.iocoder.yudao.module.game.dal.mysql.rent.RentMapper;

import cn.iocoder.yudao.module.game.service.console.clients.dst.DstClient;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.Player;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.WorldInfo;
import cn.iocoder.yudao.module.game.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    InstanceMapper instanceDao;

    @Autowired
    RentMapper userRentDao;

    @Autowired
    PlayerMapper dstPlayerDao;

    ExecutorService executor = new ThreadPoolExecutor(5, 5, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());


    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("定时检测局内玩家人数");

        List<RentDO> mbtUserRentEntities = userRentDao.selectList(new LambdaQueryWrapperX<RentDO>().gt(RentDO::getEndTime, LocalDateTime.now()).eq(RentDO::getSale, true).eq(RentDO::getGameCode, "dst"));

        DstClient client = DstClient.getInstance();
        CountDownLatch countDownLatch = new CountDownLatch(mbtUserRentEntities.size());
        for (RentDO mbtUserRentEntity : mbtUserRentEntities) {
            executor.submit(() -> {
                Long rentId = mbtUserRentEntity.getId();
                Long userId = mbtUserRentEntity.getUserId();
                Long instanceId = mbtUserRentEntity.getInstanceId();
                String ip = mbtUserRentEntity.getInstanceIp();
                InstanceDO instance = instanceDao.selectById(instanceId);
                List<Player> onlinePlayers = client.getOnlinePlayers(instance);
                int number = onlinePlayers.size();
                try {
                    if (number == 0) {
                        // 判断前一次是不是玩家人数大于0，说明玩家在刚刚的时候全部退出游戏了.
                        // todo limit 1
                        PlayerDO  mbtDstPlayerEntity =   dstPlayerDao.selectOne(new LambdaQueryWrapperX<PlayerDO>()
                                .orderByDesc(PlayerDO::getCreateTime)
                                .eq(PlayerDO::getRentId, rentId)
                                .eq(PlayerDO::getUserId, userId)
                                .last("limit 1")
                        );

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
                    PlayerDO record = new PlayerDO();
                    record.setUserId(userId);
                    record.setRentId(rentId);
                    record.setInstanceIp(ip);
                    record.setNumber(number);
                    dstPlayerDao.insert(record);
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
