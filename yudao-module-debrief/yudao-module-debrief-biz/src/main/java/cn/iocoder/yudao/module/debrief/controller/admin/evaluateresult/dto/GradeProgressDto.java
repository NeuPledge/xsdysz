package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto;

import lombok.Data;

@Data
public class GradeProgressDto {


    private Long gradeId;
    private String gradeName;
    private Long num;
    private Long total;
    private Double ratio;

}
