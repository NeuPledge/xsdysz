package cn.iocoder.yudao.module.game.controller.admin.snapshot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 快照, 便于误操作恢复数据新增/修改 Request VO")
@Data
public class SnapshotSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11397")
    private Long id;

    @Schema(description = "游戏对应code", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "游戏对应code不能为空")
    private String gameCode;

    @Schema(description = "执行动作")
    private String action;

    @Schema(description = "快照内容")
    private String snapshot;

}