package cn.iocoder.yudao.module.debrief.controller.admin.partymember.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 党员信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PartyMemberPageReqVO extends PageParam {

    @Schema(description = "学生id", example = "8048")
    private Long debriefStudentId;

    @Schema(description = "学生姓名", example = "李四")
    private String name;

    @Schema(description = "学号")
    private String studentNumber;

    @Schema(description = "支部id", example = "21034")
    private Long branchId;

    @Schema(description = "学院id", example = "24900")
    private Long collegeId;

    @Schema(description = "班级id", example = "28489")
    private Long gradeId;

    @Schema(description = "满意数")
    private Long satisfaction;

    @Schema(description = "基本满意数")
    private Long baseSatisfaction;

    @Schema(description = "不满意数")
    private Long dissatisfaction;

    @Schema(description = "最后修改时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] modifyTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}