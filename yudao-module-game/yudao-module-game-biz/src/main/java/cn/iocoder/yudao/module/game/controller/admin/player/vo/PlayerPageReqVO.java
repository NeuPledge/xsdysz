package cn.iocoder.yudao.module.game.controller.admin.player.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 记录局内玩家人数分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PlayerPageReqVO extends PageParam {

    @Schema(description = "游戏对应code")
    private String gameCode;

    @Schema(description = "用户id", example = "4535")
    private Long userId;

    @Schema(description = "用户租约id", example = "16813")
    private Long rentId;

    @Schema(description = "机器ip")
    private String instanceIp;

    @Schema(description = "玩家人数")
    private Integer number;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}