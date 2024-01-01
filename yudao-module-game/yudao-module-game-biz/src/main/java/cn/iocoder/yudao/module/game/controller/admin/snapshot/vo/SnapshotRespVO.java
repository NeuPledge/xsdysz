package cn.iocoder.yudao.module.game.controller.admin.snapshot.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 快照, 便于误操作恢复数据 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SnapshotRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11397")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "游戏对应code", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("游戏对应code")
    private String gameCode;

    @Schema(description = "执行动作")
    @ExcelProperty("执行动作")
    private String action;

    @Schema(description = "快照内容")
    @ExcelProperty("快照内容")
    private String snapshot;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}