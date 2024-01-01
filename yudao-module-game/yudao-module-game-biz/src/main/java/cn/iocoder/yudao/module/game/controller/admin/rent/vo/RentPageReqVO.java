package cn.iocoder.yudao.module.game.controller.admin.rent.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 租约分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RentPageReqVO extends PageParam {

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
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "租约到期时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] endTime;

    @Schema(description = "机器id", example = "22663")
    private Long instanceId;

    @Schema(description = "机器ip")
    private String instanceIp;

    @Schema(description = "饥荒世界名称", example = "李四")
    private String clusterName;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "是否开启自动续费")
    private Integer autoRenewal;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}