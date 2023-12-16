package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CtypeResult {

    @Schema(description = "评价类型: c1_100等，具体看一下sql的case when")
    private String ctype;

    @Schema(description = "学生类型: yanjiusheng, benke")
    private String stype;

    @Schema(description = "人数")
    private Integer num;

    @Schema(description = "人数占比")
    private Double rate;

}
