package cn.iocoder.yudao.module.game.common.response;

import lombok.Data;

/**
 * 集群状态操作.
 */
@Data
public class ClusterStatusOperation {

    private Status status;

    public enum Status {
        QUERY_STATUS,       // 查询状态
        START_ALL,          // 开启(如已启动会自动重启)全部
        STOP_ALL,          // 关闭全部
        START_MASTER,       // 开启地面世界
        STOP_MASTER,        // 关闭地面世界
        START_CAVES,        // 开启洞穴世界
        STOP_CAVES,         // 关闭洞穴世界
    }

}
