package cn.iocoder.yudao.module.game.controller.admin.dstblockmod.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 模组黑名单,此名单上的模组,将会警告提示/禁止添加分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DstBlockModPageReqVO extends PageParam {

    @Schema(description = "模组id(不含workshop-)", example = "20369")
    private Long modId;

    @Schema(description = "模组名称")
    private String modTitle;

    @Schema(description = "拉黑理由")
    private String desc;

    @Schema(description = "warn/deny", example = "1")
    private String status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}