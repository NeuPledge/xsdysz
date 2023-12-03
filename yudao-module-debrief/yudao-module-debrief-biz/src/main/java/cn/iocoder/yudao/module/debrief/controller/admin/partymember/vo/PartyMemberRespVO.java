package cn.iocoder.yudao.module.debrief.controller.admin.partymember.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 党员信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PartyMemberRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "27372")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "学生id", example = "8048")
    @ExcelProperty("学生id")
    private Long debriefStudentId;

    @Schema(description = "学生姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("学生姓名")
    private String name;

    @Schema(description = "学号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("学号")
    private String studentNumber;

    @Schema(description = "支部id", example = "21034")
    @ExcelProperty("支部id")
    private Long branchId;

    @Schema(description = "学院id", requiredMode = Schema.RequiredMode.REQUIRED, example = "24900")
    @ExcelProperty("学院id")
    private Long collegeId;

    @Schema(description = "班级id", requiredMode = Schema.RequiredMode.REQUIRED, example = "28489")
    @ExcelProperty("班级id")
    private Long gradeId;

    @Schema(description = "满意数")
    @ExcelProperty("满意数")
    private Long satisfaction;

    @Schema(description = "基本满意数")
    @ExcelProperty("基本满意数")
    private Long baseSatisfaction;

    @Schema(description = "不满意数")
    @ExcelProperty("不满意数")
    private Long dissatisfaction;

    @Schema(description = "最后修改时间")
    @ExcelProperty("最后修改时间")
    private LocalDateTime modifyTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}