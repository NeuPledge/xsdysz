package cn.iocoder.yudao.module.game.service.console.dst.timer;

import cn.hutool.core.date.StopWatch;
import com.dooyo.dao.MBTInstanceDao;
import com.dooyo.dao.MBTUserRentDao;
import com.dooyo.dao.entity.MBTInstanceEntity;
import com.dooyo.dao.entity.MBTUserRentEntity;
import com.dooyo.dao.entity.MBTUserRentEntityExample;
import com.dooyo.dao.entity.MBTUserRentEntityExample.Criteria;
import com.dooyo.service.clients.dst.DstClient;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.WorldInfo;
import cn.iocoder.yudao.module.game.util.DateFormatUtil;
import cn.iocoder.yudao.module.game.util.DateFormatUtil.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BackUpArchiveService {

    @Autowired
    MBTInstanceDao instanceDao;

    @Autowired
    MBTUserRentDao userRentDao;

    public void run() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("定时创建存档");

        MBTUserRentEntityExample example = new MBTUserRentEntityExample();
        Criteria criteria = example.createCriteria();
        criteria.andEndTimeGreaterThan(new Date());
        criteria.andSaleEqualTo(true);
        List<MBTUserRentEntity> mbtUserRentEntities = userRentDao.selectByExample(example);
        DstClient client = DstClient.getInstance();
        for (MBTUserRentEntity mbtUserRentEntity : mbtUserRentEntities) {
            try {
                MBTInstanceEntity instance = instanceDao.selectByPrimaryKey(mbtUserRentEntity.getInstanceId());
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
