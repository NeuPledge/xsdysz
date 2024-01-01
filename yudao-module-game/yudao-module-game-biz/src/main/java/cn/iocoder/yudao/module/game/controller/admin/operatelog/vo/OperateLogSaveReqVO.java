package cn.iocoder.yudao.module.game.controller.admin.operatelog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 操作日志新增/修改 Request VO")
@Data
public class OperateLogSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14719")
    private Long id;

    @Schema(description = "游戏对应code", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "游戏对应code不能为空")
    private String gameCode;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11622")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "租约id", example = "13050")
    private Long rentId;

    @Schema(description = "机器id", example = "30187")
    private Long instanceId;

    @Schema(description = "公网ip")
    private String instanceIp;

    @Schema(description = "操作名称", example = "赵六")
    private String name;

}