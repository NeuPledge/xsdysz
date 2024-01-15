package cn.iocoder.yudao.module.game.service.console.dst.timer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DstRentHostStopService {
/*
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

    }*/

}
