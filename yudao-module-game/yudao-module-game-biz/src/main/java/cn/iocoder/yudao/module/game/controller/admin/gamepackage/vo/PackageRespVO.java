package cn.iocoder.yudao.module.game.controller.admin.gamepackage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 饥荒套餐配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PackageRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9645")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "游戏对应code", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("游戏对应code")
    private String gameCode;

    @Schema(description = "套餐版本")
    @ExcelProperty("套餐版本")
    private Integer packageVersion;

    @Schema(description = "套餐编号", example = "3220")
    @ExcelProperty("套餐编号")
    private Integer packageId;

    @Schema(description = "套餐名称", example = "张三")
    @ExcelProperty("套餐名称")
    private String name;

    @Schema(description = "套餐描述")
    @ExcelProperty("套餐描述")
    private String desc;

    @Schema(description = "原价格元/月", requiredMode = Schema.RequiredMode.REQUIRED, example = "4661")
    @ExcelProperty("原价格元/月")
    private Double price;

    @Schema(description = "玩家人数")
    @ExcelProperty("玩家人数")
    private Integer player;

    @Schema(description = "核心数")
    @ExcelProperty("核心数")
    private Integer cpu;

    @Schema(description = "内存,单位G(上限)")
    @ExcelProperty("内存,单位G(上限)")
    private Double memoryMax;

    @Schema(description = "内存,单位G(下限)")
    @ExcelProperty("内存,单位G(下限)")
    private Double memoryMin;

    @Schema(description = "cpu主频率(下限)")
    @ExcelProperty("cpu主频率(下限)")
    private Double cpuHzMin;

    @Schema(description = "cpu主频率(上限)")
    @ExcelProperty("cpu主频率(上限)")
    private Double cpuHzMax;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}