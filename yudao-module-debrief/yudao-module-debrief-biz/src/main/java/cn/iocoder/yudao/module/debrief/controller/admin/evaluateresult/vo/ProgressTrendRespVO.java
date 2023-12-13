package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 党员完成趋势")
@Data
@ExcelIgnoreUnannotated
public class ProgressTrendRespVO {

    private List<String> days;
    private List<Long> dayCounts;
    private List<Long> totalCounts;
}
