package cn.iocoder.yudao.module.debrief.controller.admin.diccollege.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 学院字典列 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DicCollegeRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "11761")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "学院名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("学院名")
    private String collegeName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}