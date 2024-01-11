package cn.iocoder.yudao.module.game.common.params;

import cn.iocoder.yudao.module.game.common.enums.GameCode;
import lombok.Data;

@Data
public class FeedbackParam {

    private GameCode gameCode;

    private String content;

}
