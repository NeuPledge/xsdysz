package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 评价结果 Response VO")
@Data
@ExcelIgnoreUnannotated
public class EvaluateResultRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22297")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "被评议党员id(debrief_party_member表的主键)", requiredMode = Schema.RequiredMode.REQUIRED, example = "17375")
    @ExcelProperty("被评议党员id(debrief_party_member表的主键)")
    private Long partyMemberId;

    @Schema(description = "评议者id（debrief_student表的主键）", requiredMode = Schema.RequiredMode.REQUIRED, example = "26557")
    @ExcelProperty("评议者id（debrief_student表的主键）")
    private Long commenterId;

    @Schema(description = "1.满意; 2.基本满意; 3.不满意", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("1.满意; 2.基本满意; 3.不满意")
    private Long comment;

    @Schema(description = "支部id", example = "3840")
    @ExcelProperty("支部id")
    private Long branchId;

    @Schema(description = "学院id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12314")
    @ExcelProperty("学院id")
    private Long collegeId;

    @Schema(description = "班级id", requiredMode = Schema.RequiredMode.REQUIRED, example = "31662")
    @ExcelProperty("班级id")
    private Long gradeId;

    @Schema(description = "其他评价内容")
    @ExcelProperty("其他评价内容")
    private String content;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}