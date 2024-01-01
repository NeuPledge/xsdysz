package cn.iocoder.yudao.module.game.controller.admin.gamepackage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 饥荒套餐配置新增/修改 Request VO")
@Data
public class PackageSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9645")
    private Long id;

    @Schema(description = "游戏对应code", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "游戏对应code不能为空")
    private String gameCode;

    @Schema(description = "套餐版本")
    private Integer packageVersion;

    @Schema(description = "套餐编号", example = "3220")
    private Integer packageId;

    @Schema(description = "套餐名称", example = "张三")
    private String name;

    @Schema(description = "套餐描述")
    private String desc;

    @Schema(description = "原价格元/月", requiredMode = Schema.RequiredMode.REQUIRED, example = "4661")
    @NotNull(message = "原价格元/月不能为空")
    private Double price;

    @Schema(description = "玩家人数")
    private Integer player;

    @Schema(description = "核心数")
    private Integer cpu;

    @Schema(description = "内存,单位G(上限)")
    private Double memoryMax;

    @Schema(description = "内存,单位G(下限)")
    private Double memoryMin;

    @Schema(description = "cpu主频率(下限)")
    private Double cpuHzMin;

    @Schema(description = "cpu主频率(上限)")
    private Double cpuHzMax;

}