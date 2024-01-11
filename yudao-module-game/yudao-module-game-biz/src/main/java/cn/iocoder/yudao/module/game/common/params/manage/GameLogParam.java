package cn.iocoder.yudao.module.game.common.params.manage;

import cn.iocoder.yudao.module.game.common.enums.dst.LogType;
import lombok.Data;

@Data
public class GameLogParam {
    
    private Long instanceId;
    private LogType logType;
    private Integer line;
}
