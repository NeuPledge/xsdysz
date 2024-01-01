package cn.iocoder.yudao.module.game.controller.admin.rent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 租约 Response VO")
@Data
@ExcelIgnoreUnannotated
public class RentRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19483")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "登录账号", example = "15404")
    @ExcelProperty("登录账号")
    private Long userId;

    @Schema(description = "是否已售, 0:未售, 1:已售")
    @ExcelProperty("是否已售, 0:未售, 1:已售")
    private Boolean sale;

    @Schema(description = "套餐ID", example = "7835")
    @ExcelProperty("套餐ID")
    private Long packageId;

    @Schema(description = "游戏对应code")
    @ExcelProperty("游戏对应code")
    private String gameCode;

    @Schema(description = "最大玩家人数")
    @ExcelProperty("最大玩家人数")
    private Integer playerLimit;

    @Schema(description = "租约开始时间")
    @ExcelProperty("租约开始时间")
    private LocalDateTime startTime;

    @Schema(description = "租约到期时间")
    @ExcelProperty("租约到期时间")
    private LocalDateTime endTime;

    @Schema(description = "机器id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22663")
    @ExcelProperty("机器id")
    private Long instanceId;

    @Schema(description = "机器ip", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("机器ip")
    private String instanceIp;

    @Schema(description = "饥荒世界名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("饥荒世界名称")
    private String clusterName;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String notes;

    @Schema(description = "是否开启自动续费", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否开启自动续费")
    private Integer autoRenewal;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}