package cn.iocoder.yudao.module.debrief.controller.admin.diccollege.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StypeCount {

    @Schema(description = "学生类型: yanjiusheng, benke")
    private String stype;

    @Schema(description = "人数")
    private Integer num;

}
