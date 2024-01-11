package cn.iocoder.yudao.module.game.common.params;

import lombok.Data;

import java.util.List;

@Data
public class Heartbeat {
    Long instanceId;
    String ip;
    AgentHostInfo agentHostInfo;

    @Data
    public static class AgentHostInfo{
        Integer MemoryTotal;
        Integer MemoryFree;
        Integer MemoryUsed;
        Integer SwapMemoryTotal;
        Integer SwapMemoryFree;
        Integer SwapMemoryUsed;
        List<Float> CpuUsedPercent;
        Integer TotalCpuUsedPercent;
        Integer DiskTotal;
        Integer DiskUsed;
        Integer NetworkSpeedIn;
        Integer NetworkSpeedOut;
        Integer NetworkName;
    }
}
