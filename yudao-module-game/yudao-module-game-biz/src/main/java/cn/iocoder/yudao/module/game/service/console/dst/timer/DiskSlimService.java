package cn.iocoder.yudao.module.game.service.console.dst.timer;


import cn.hutool.core.date.StopWatch;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.dal.mysql.instance.InstanceMapper;
import cn.iocoder.yudao.module.game.util.Shell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

@Slf4j
@Service
public class DiskSlimService {

    @Autowired
    InstanceMapper instanceDao;

    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("磁盘检测并瘦身");

        List<InstanceDO> mbtInstanceEntities = instanceDao.selectList(new LambdaQueryWrapperX<InstanceDO>().eq(InstanceDO::getAgentStatus, "up").gt(InstanceDO::getEndTime, LocalDateTime.now()));

        CountDownLatch countDownLatch = new CountDownLatch(mbtInstanceEntities.size());
        ExecutorService executor = new ThreadPoolExecutor(30, 30, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

        for (InstanceDO mbtInstanceEntity : mbtInstanceEntities) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        String ip = mbtInstanceEntity.getIp();
                        Shell shell = new Shell(ip, "root", mbtInstanceEntity.getSshPort());
                        // 先清理空间
                        shell.execCommandPrvKey("rm -rf /root/dstserver/data/core.*");
                        String result = shell.execCommandPrvKey("df -mh / | sed '1d'");
                        // 天翼云 /dev/vda2        36G   29G  5.4G  84% /
                        // 腾讯云 /dev/vda1        59G   19G   38G  33% /
                        //       /dev/vda1        40G   11G   27G  30% /
                        // 指点云 /dev/sda1        40G   20G   19G  52% /
                        if (StringUtils.isEmpty(result)) {
                            // 未获取到/获取失败
                            log.error("获取磁盘容量失败, 机器id:{}, ip:{}", mbtInstanceEntity.getId(), ip);
                        } else {
                            String[] items = result.split("\s+");
                            String diskStr = items[1];
                            String diskFreePercentStr = items[4];
                            int disk = Integer.parseInt(diskStr.replace("G", ""));
                            int diskFreePercent = Integer.parseInt(diskFreePercentStr.replace("%", ""));
//                            InstanceDO instance = new InstanceDO();
                            mbtInstanceEntity.setDisk(disk);
                            mbtInstanceEntity.setDiskUsedPercent(diskFreePercent);
                            mbtInstanceEntity.setId(mbtInstanceEntity.getId());
                            instanceDao.updateById(mbtInstanceEntity);
                        }
                    } catch (Exception ignore) {
                        log.error("获取磁盘容量失败, 机器id:{}, ip:{}", mbtInstanceEntity.getId(), mbtInstanceEntity.getIp());
                    } finally {
                        countDownLatch.countDown();
                        log.info("剩余:" + String.valueOf(countDownLatch.getCount()));
                    }
                }
            });
        }
        try {
            countDownLatch.await(2, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        stopWatch.stop();
        log.info("磁盘检测并瘦身, 耗时:" + stopWatch.getTotalTimeSeconds() + "秒");

    }
}
