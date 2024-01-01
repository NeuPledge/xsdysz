package cn.iocoder.yudao.module.game.controller.admin.rent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 租约新增/修改 Request VO")
@Data
public class RentSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19483")
    private Long id;

    @Schema(description = "登录账号", example = "15404")
    private Long userId;

    @Schema(description = "是否已售, 0:未售, 1:已售")
    private Boolean sale;

    @Schema(description = "套餐ID", example = "7835")
    private Long packageId;

    @Schema(description = "游戏对应code")
    private String gameCode;

    @Schema(description = "最大玩家人数")
    private Integer playerLimit;

    @Schema(description = "租约开始时间")
    private LocalDateTime startTime;

    @Schema(description = "租约到期时间")
    private LocalDateTime endTime;

    @Schema(description = "机器id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22663")
    @NotNull(message = "机器id不能为空")
    private Long instanceId;

    @Schema(description = "机器ip", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "机器ip不能为空")
    private String instanceIp;

    @Schema(description = "饥荒世界名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "饥荒世界名称不能为空")
    private String clusterName;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "是否开启自动续费", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "是否开启自动续费不能为空")
    private Integer autoRenewal;

}