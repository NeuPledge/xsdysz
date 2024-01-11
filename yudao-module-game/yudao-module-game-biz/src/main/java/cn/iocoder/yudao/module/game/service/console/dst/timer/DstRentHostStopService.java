package cn.iocoder.yudao.module.game.service.console.dst.timer;


import cn.iocoder.yudao.module.game.common.params.manage.InstanceQueryParam;
import cn.iocoder.yudao.module.game.common.response.InstanceInfo;
import com.dooyo.service.IInstanceService;
import com.dooyo.service.clients.dst.DstClient;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.WorldStatus;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.module.game.common.params.manage.InstanceQueryParam.State.IDLE;

@Slf4j
@Service
public class DstRentHostStopService {

    @Resource
    IInstanceService instanceService;

    public void stopExpiredHost() {
        InstanceQueryParam param = new InstanceQueryParam();
        param.setValid(true);
        // 列出空闲的机器
        param.setState(IDLE);
        param.setPageNum(1);
        param.setPageSize(10000);
        param.setSortBy("id");
        param.setOrder("asc");

        PageInfo list = instanceService.list(param);
        List<InstanceInfo> instanceInfos = list.getList();
        for (InstanceInfo instance : instanceInfos) {

            Date rentEndTime = instance.getRentEndTime();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rentEndTime);
            calendar.add(Calendar.DATE, 10);
            if (calendar.before(new Date())) {
                continue;
            }
            // 只针对已经过期的天数在10天之内的机器，做强制stop处理.
            DstClient client = DstClient.getInstance();
            try {
                WorldStatus worldStatus = client.queryWorldsStatus(instance);
                if (worldStatus.getIsCavesExist()) {
                    client.stopCaves(instance);
                }
                if (worldStatus.getIsMasterExist()) {
                    client.stopMaster(instance);
                }
            } catch (Exception ignore) {
            }
        }

    }

}
