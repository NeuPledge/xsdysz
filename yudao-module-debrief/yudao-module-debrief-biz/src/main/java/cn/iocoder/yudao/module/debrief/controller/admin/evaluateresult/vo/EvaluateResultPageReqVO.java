package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 评价结果分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateResultPageReqVO extends PageParam {

    @Schema(description = "被评议党员id(debrief_party_member表的主键)", example = "17375")
    private Long partyMemberId;

    @Schema(description = "评议者id（debrief_student表的主键）", example = "26557")
    private Long commenterId;

    @Schema(description = "1.满意; 2.基本满意; 3.不满意")
    private Long comment;

    @Schema(description = "支部id", example = "3840")
    private Long branchId;

    @Schema(description = "学院id", example = "12314")
    private Long collegeId;

    @Schema(description = "班级id", example = "31662")
    private Long gradeId;

    @Schema(description = "其他评价内容")
    private String content;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}