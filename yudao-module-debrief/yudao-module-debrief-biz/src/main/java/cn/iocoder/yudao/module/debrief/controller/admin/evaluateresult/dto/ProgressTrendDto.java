package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 党员完成趋势")
@Data
@ExcelIgnoreUnannotated
public class ProgressTrendDto {

    @Schema(description = "日期")
    private String day;

    @Schema(description = "当日数量")
    private Long dayCount;

    @Schema(description = "累计数量")
    private Long totalCount;

}