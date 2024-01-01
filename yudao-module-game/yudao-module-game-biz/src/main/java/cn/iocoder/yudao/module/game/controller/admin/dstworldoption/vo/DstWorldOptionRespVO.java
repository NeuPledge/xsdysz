package cn.iocoder.yudao.module.game.controller.admin.dstworldoption.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 饥荒世界配置初始化数据 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DstWorldOptionRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24573")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "世界类型master/caves")
    @ExcelProperty("世界类型master/caves")
    private String world;

    @Schema(description = "配置类型, 世界选项/世界生成", example = "1")
    @ExcelProperty("配置类型, 世界选项/世界生成")
    private String configType;

    @Schema(description = "分类(生物/资源/...)", example = "2")
    @ExcelProperty("分类(生物/资源/...)")
    private String resourceType;

    @Schema(description = "配置标题(human read)")
    @ExcelProperty("配置标题(human read)")
    private String label;

    @Schema(description = "是否是全局配置")
    @ExcelProperty("是否是全局配置")
    private Integer globalConfig;

    @Schema(description = "启用", example = "2")
    @ExcelProperty("启用")
    private Integer status;

    @Schema(description = "排序")
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "配置name(key)", example = "芋艿")
    @ExcelProperty("配置name(key)")
    private String keyName;

    @Schema(description = "图标")
    @ExcelProperty("图标")
    private String icon;

    @Schema(description = "可选项(json数组)")
    @ExcelProperty("可选项(json数组)")
    private String options;

    @Schema(description = "默认选项")
    @ExcelProperty("默认选项")
    private String defaultOption;

    @Schema(description = "推荐选项(同理可以添加其他类型的选项)")
    @ExcelProperty("推荐选项(同理可以添加其他类型的选项)")
    private String recommendOption;

    @Schema(description = "轻松模式下的选项")
    @ExcelProperty("轻松模式下的选项")
    private String relaxedOption;

    @Schema(description = "无尽模式下的选项")
    @ExcelProperty("无尽模式下的选项")
    private String endlessOption;

    @Schema(description = "生存模式下的选项")
    @ExcelProperty("生存模式下的选项")
    private String survivalOption;

    @Schema(description = "荒野模式下的选项")
    @ExcelProperty("荒野模式下的选项")
    private String wildernessOption;

    @Schema(description = "暗无天日模式下的选项")
    @ExcelProperty("暗无天日模式下的选项")
    private String lightsoutOption;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}