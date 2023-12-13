package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 党员完成趋势")
@Data
@ExcelIgnoreUnannotated
public class CommentTrendDto {

    @Schema(description = "日期")
    private String day;

    @Schema(description = "评论类型")
    private Integer comment;

    @Schema(description = "数量")
    private Long num;

}