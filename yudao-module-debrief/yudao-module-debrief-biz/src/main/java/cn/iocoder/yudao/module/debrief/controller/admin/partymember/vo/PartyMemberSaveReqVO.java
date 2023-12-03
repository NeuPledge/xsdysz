package cn.iocoder.yudao.module.debrief.controller.admin.partymember.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 党员信息新增/修改 Request VO")
@Data
public class PartyMemberSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "27372")
    private Long id;

    @Schema(description = "学生id", example = "8048")
    private Long debriefStudentId;

    @Schema(description = "学生姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "学生姓名不能为空")
    private String name;

    @Schema(description = "学号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "学号不能为空")
    private String studentNumber;

    @Schema(description = "支部id", example = "21034")
    private Long branchId;

    @Schema(description = "学院id", requiredMode = Schema.RequiredMode.REQUIRED, example = "24900")
    @NotNull(message = "学院id不能为空")
    private Long collegeId;

    @Schema(description = "班级id", requiredMode = Schema.RequiredMode.REQUIRED, example = "28489")
    @NotNull(message = "班级id不能为空")
    private Long gradeId;

    @Schema(description = "满意数")
    private Long satisfaction;

    @Schema(description = "基本满意数")
    private Long baseSatisfaction;

    @Schema(description = "不满意数")
    private Long dissatisfaction;

    @Schema(description = "最后修改时间")
    private LocalDateTime modifyTime;

}