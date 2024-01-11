package cn.iocoder.yudao.module.game.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BlockModResponse {

    @Schema(description = "被拉黑状态, null=未拉黑 ,deny=禁止, warn=警告但允许添加")
    private String status;

    private String modId;

    private String reason;
}
