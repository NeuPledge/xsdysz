package cn.iocoder.yudao.module.game.service.console.dst.timer;

import cn.hutool.core.date.StopWatch;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;
import cn.iocoder.yudao.module.game.dal.mysql.instance.InstanceMapper;
import cn.iocoder.yudao.module.game.dal.mysql.rent.RentMapper;
import cn.iocoder.yudao.module.game.service.console.clients.dst.DstClient;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.WorldInfo;
import cn.iocoder.yudao.module.game.util.DateFormatUtil;
import cn.iocoder.yudao.module.game.util.DateFormatUtil.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BackUpArchiveService {

    @Autowired
    InstanceMapper instanceDao;

    @Autowired
    RentMapper userRentDao;

    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("定时创建存档");

        List<RentDO> rentDOS = userRentDao.selectList(new LambdaQueryWrapperX<RentDO>().gt(RentDO::getEndTime, LocalDateTime.now()).eq(RentDO::getSale, true));

        DstClient client = DstClient.getInstance();
        for (RentDO mbtUserRentEntity : rentDOS) {
            try {
                InstanceDO instance = instanceDao.selectById(mbtUserRentEntity.getInstanceId());
                WorldInfo worldInfo = client.getWorldInfo(instance);
                String nowDateTime = DateFormatUtil.formatDateTime(new Date(), Pattern._41);
                String fileName = "存档备份__租约ID" + mbtUserRentEntity.getId() + "__世界第" + (worldInfo.getClock().getCycles() + 1) + "天__" + nowDateTime + ".zip";
                client.zipArchive(instance, fileName);
                log.info("租约ID:{}, 用户ID:{}, 机器IP:{}, 定时创建存档成功:{}", mbtUserRentEntity.getId(), mbtUserRentEntity.getUserId(), mbtUserRentEntity.getInstanceIp(), fileName);
            } catch (Exception exception) {
                log.info("租约ID:{}, 用户ID:{}, 机器IP:{}, 定时创建存档失败, msg:{}", mbtUserRentEntity.getId(), mbtUserRentEntity.getUserId(), mbtUserRentEntity.getInstanceIp(), exception.getMessage());
            }
        }

        stopWatch.stop();
        log.info("定时创建存档, 耗时:" + stopWatch.getTotalTimeSeconds() + "秒");
    }

}
