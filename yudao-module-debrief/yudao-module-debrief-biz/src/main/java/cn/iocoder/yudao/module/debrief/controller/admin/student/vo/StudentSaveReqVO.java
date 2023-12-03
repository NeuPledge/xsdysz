package cn.iocoder.yudao.module.debrief.controller.admin.student.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 学生信息新增/修改 Request VO")
@Data
public class StudentSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "7332")
    private Long id;

    @Schema(description = "学生姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "学生姓名不能为空")
    private String name;

    @Schema(description = "学号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "学号不能为空")
    private String studentNumber;

    @Schema(description = "班级id", example = "28056")
    private Long gradeId;

    @Schema(description = "班级", example = "张三")
    private String gradeName;

    @Schema(description = "学院id", requiredMode = Schema.RequiredMode.REQUIRED, example = "13814")
    @NotNull(message = "学院id不能为空")
    private Long collegeId;

}