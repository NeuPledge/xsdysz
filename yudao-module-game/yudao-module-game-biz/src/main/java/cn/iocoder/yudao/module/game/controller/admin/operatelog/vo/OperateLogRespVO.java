package cn.iocoder.yudao.module.game.controller.admin.operatelog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 操作日志 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OperateLogRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14719")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "游戏对应code", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("游戏对应code")
    private String gameCode;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "11622")
    @ExcelProperty("用户id")
    private Long userId;

    @Schema(description = "租约id", example = "13050")
    @ExcelProperty("租约id")
    private Long rentId;

    @Schema(description = "机器id", example = "30187")
    @ExcelProperty("机器id")
    private Long instanceId;

    @Schema(description = "公网ip")
    @ExcelProperty("公网ip")
    private String instanceIp;

    @Schema(description = "操作名称", example = "赵六")
    @ExcelProperty("操作名称")
    private String name;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}