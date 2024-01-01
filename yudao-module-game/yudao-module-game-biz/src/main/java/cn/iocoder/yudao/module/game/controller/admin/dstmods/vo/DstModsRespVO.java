package cn.iocoder.yudao.module.game.controller.admin.dstmods.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 饥荒模组信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DstModsRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16395")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "模组id(不含workshop-)", example = "23018")
    @ExcelProperty("模组id(不含workshop-)")
    private Long modId;

    @Schema(description = "模组名称")
    @ExcelProperty("模组名称")
    private String modTitle;

    @Schema(description = "是否为服务端模组")
    @ExcelProperty("是否为服务端模组")
    private Integer isServerMod;

    @Schema(description = "模组steam链接", example = "https://www.iocoder.cn")
    @ExcelProperty("模组steam链接")
    private String url;

    @Schema(description = "标签")
    @ExcelProperty("标签")
    private String tags;

    @Schema(description = "依赖的模组")
    @ExcelProperty("依赖的模组")
    private String dependMods;

    @Schema(description = "模组图标")
    @ExcelProperty("模组图标")
    private String image;

    @Schema(description = "模组评分星级图标", example = "https://www.iocoder.cn")
    @ExcelProperty("模组评分星级图标")
    private String starUrl;

    @Schema(description = "作者")
    @ExcelProperty("作者")
    private String author;

    @Schema(description = "版本")
    @ExcelProperty("版本")
    private String version;

    @Schema(description = "文件大小")
    @ExcelProperty("文件大小")
    private String fileSize;

    @Schema(description = "模组发布时间")
    @ExcelProperty("模组发布时间")
    private String modPublishTime;

    @Schema(description = "模组更新时间")
    @ExcelProperty("模组更新时间")
    private String modUpdateTime;

    @Schema(description = "模组评分星级")
    @ExcelProperty("模组评分星级")
    private Integer star;

    @Schema(description = "模组状态,0:不可用,1:可用", example = "1")
    @ExcelProperty("模组状态,0:不可用,1:可用")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}