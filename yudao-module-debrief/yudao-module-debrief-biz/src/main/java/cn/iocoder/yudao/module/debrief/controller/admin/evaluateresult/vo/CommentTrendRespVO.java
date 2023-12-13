package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 近30日测评票数趋势")
@Data
@ExcelIgnoreUnannotated
public class CommentTrendRespVO {

    private List<String> days;
    private List<Long> comment1;
    private List<Long> comment2;
    private List<Long> comment3;
}
