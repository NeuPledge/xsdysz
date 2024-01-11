package cn.iocoder.yudao.module.game.common.response;

import lombok.Data;

import java.util.List;

@Data
public class HostResourceInfo {
    private Double memoryTotal;
    private Double memoryFree;
    private Double memoryUsed;
    private Double swapMemoryTotal;
    private Double swapMemoryFree;
    private Double swapMemoryUsed;
    private List<Double> cpuUsedPercent;
    private Double totalCpuUsedPercent;
    private Double diskTotal;
    private Double diskUsed;
    private Double networkSpeedIn;
    private Double networkSpeedOut;
    private String networkName;
}
