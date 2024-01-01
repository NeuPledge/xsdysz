package cn.iocoder.yudao.module.game.controller.admin.player.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 记录局内玩家人数 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PlayerRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13856")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "游戏对应code", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("游戏对应code")
    private String gameCode;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4535")
    @ExcelProperty("用户id")
    private Long userId;

    @Schema(description = "用户租约id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16813")
    @ExcelProperty("用户租约id")
    private Long rentId;

    @Schema(description = "机器ip")
    @ExcelProperty("机器ip")
    private String instanceIp;

    @Schema(description = "玩家人数")
    @ExcelProperty("玩家人数")
    private Integer number;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}