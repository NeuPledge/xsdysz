package cn.iocoder.yudao.module.game.controller.admin.player.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 记录局内玩家人数新增/修改 Request VO")
@Data
public class PlayerSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13856")
    private Long id;

    @Schema(description = "游戏对应code", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "游戏对应code不能为空")
    private String gameCode;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4535")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "用户租约id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16813")
    @NotNull(message = "用户租约id不能为空")
    private Long rentId;

    @Schema(description = "机器ip")
    private String instanceIp;

    @Schema(description = "玩家人数")
    private Integer number;

}