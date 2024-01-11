package cn.iocoder.yudao.module.game.service.console.clients.dst.model;

import lombok.Data;


/**
 * {
 * "Master":"{\"RssMem\":0,\"MemRate\":0,\"VszMem\":129056,\"PName\":\"Master\",\"Pid\":2897,\"CpuRate\":0}",
 * "Caves":"{\"RssMem\":0,\"MemRate\":0,\"VszMem\":0,\"PName\":\"\",\"Pid\":0,\"CpuRate\":0}"
 * }
 */

@Data
public class ProcessInfo {

    private Boolean isRunning;
    private ProcessItem master;
    private ProcessItem caves;

    @Data
    public static class ProcessItem {

        private Boolean isRunning;
        private double rssMem;
        private double memRate;
        private int vszMem;
        private String pName;
        private int pid;
        private double cpuRate;
    }
}


