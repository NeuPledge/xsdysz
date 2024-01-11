package cn.iocoder.yudao.module.game.common.params;

import cn.iocoder.yudao.module.game.common.enums.dst.PlayerFileType;
import lombok.Data;

@Data
public class UpdatePlayerFileParams {

    private PlayerFileType type;

    private String content;
}
