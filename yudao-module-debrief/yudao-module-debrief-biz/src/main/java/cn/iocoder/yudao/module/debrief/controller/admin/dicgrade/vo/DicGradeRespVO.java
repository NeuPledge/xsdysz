package cn.iocoder.yudao.module.debrief.controller.admin.dicgrade.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 班级字典列 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DicGradeRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "28378")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "学院id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23185")
    @ExcelProperty("学院id")
    private Long collegeId;

    @Schema(description = "学院名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "23185")
    @ExcelProperty("学院名称")
    private String collegeName;

    @Schema(description = "班级名", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("班级名")
    private String gradeName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}