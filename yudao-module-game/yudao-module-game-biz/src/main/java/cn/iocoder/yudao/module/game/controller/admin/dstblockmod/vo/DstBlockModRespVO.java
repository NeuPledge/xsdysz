package cn.iocoder.yudao.module.game.controller.admin.dstblockmod.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 模组黑名单,此名单上的模组,将会警告提示/禁止添加 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DstBlockModRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27738")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "模组id(不含workshop-)", example = "20369")
    @ExcelProperty("模组id(不含workshop-)")
    private Long modId;

    @Schema(description = "模组名称")
    @ExcelProperty("模组名称")
    private String modTitle;

    @Schema(description = "拉黑理由")
    @ExcelProperty("拉黑理由")
    private String desc;

    @Schema(description = "warn/deny", example = "1")
    @ExcelProperty("warn/deny")
    private String status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}