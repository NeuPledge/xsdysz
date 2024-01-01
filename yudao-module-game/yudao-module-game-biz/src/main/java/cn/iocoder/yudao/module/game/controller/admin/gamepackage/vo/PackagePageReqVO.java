package cn.iocoder.yudao.module.game.controller.admin.gamepackage.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 饥荒套餐配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PackagePageReqVO extends PageParam {

    @Schema(description = "游戏对应code")
    private String gameCode;

    @Schema(description = "套餐版本")
    private Integer packageVersion;

    @Schema(description = "套餐编号", example = "3220")
    private Integer packageId;

    @Schema(description = "套餐名称", example = "张三")
    private String name;

    @Schema(description = "套餐描述")
    private String desc;

    @Schema(description = "原价格元/月", example = "4661")
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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}