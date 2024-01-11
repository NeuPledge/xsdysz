package cn.iocoder.yudao.module.game.common.params.manage;

import cn.iocoder.yudao.module.game.common.enums.AgentStatus;
import lombok.Data;

@Data
public class AgentStatusParam {

    private Long instanceId;

    private AgentStatus status;

}
