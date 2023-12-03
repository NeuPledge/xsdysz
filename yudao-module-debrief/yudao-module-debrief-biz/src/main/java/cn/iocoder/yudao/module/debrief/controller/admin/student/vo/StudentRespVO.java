package cn.iocoder.yudao.module.debrief.controller.admin.student.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 学生信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class StudentRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "7332")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "学生姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("学生姓名")
    private String name;

    @Schema(description = "学号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("学号")
    private String studentNumber;

    @Schema(description = "班级id", example = "28056")
    @ExcelProperty("班级id")
    private Long gradeId;

    @Schema(description = "班级", example = "张三")
    @ExcelProperty("班级")
    private String gradeName;

    @Schema(description = "学院id", requiredMode = Schema.RequiredMode.REQUIRED, example = "13814")
    @ExcelProperty("学院id")
    private Long collegeId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}