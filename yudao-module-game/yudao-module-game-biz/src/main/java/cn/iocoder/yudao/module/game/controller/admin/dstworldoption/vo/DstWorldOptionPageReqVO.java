package cn.iocoder.yudao.module.game.controller.admin.dstworldoption.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 饥荒世界配置初始化数据分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DstWorldOptionPageReqVO extends PageParam {

    @Schema(description = "世界类型master/caves")
    private String world;

    @Schema(description = "配置类型, 世界选项/世界生成", example = "1")
    private String configType;

    @Schema(description = "分类(生物/资源/...)", example = "2")
    private String resourceType;

    @Schema(description = "配置标题(human read)")
    private String label;

    @Schema(description = "是否是全局配置")
    private Integer globalConfig;

    @Schema(description = "启用", example = "2")
    private Integer status;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "配置name(key)", example = "芋艿")
    private String keyName;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "可选项(json数组)")
    private String options;

    @Schema(description = "默认选项")
    private String defaultOption;

    @Schema(description = "推荐选项(同理可以添加其他类型的选项)")
    private String recommendOption;

    @Schema(description = "轻松模式下的选项")
    private String relaxedOption;

    @Schema(description = "无尽模式下的选项")
    private String endlessOption;

    @Schema(description = "生存模式下的选项")
    private String survivalOption;

    @Schema(description = "荒野模式下的选项")
    private String wildernessOption;

    @Schema(description = "暗无天日模式下的选项")
    private String lightsoutOption;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}