package cn.iocoder.yudao.module.game.controller.admin.operatelog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 操作日志分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OperateLogPageReqVO extends PageParam {

    @Schema(description = "游戏对应code")
    private String gameCode;

    @Schema(description = "用户id", example = "11622")
    private Long userId;

    @Schema(description = "租约id", example = "13050")
    private Long rentId;

    @Schema(description = "机器id", example = "30187")
    private Long instanceId;

    @Schema(description = "公网ip")
    private String instanceIp;

    @Schema(description = "操作名称", example = "赵六")
    private String name;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}