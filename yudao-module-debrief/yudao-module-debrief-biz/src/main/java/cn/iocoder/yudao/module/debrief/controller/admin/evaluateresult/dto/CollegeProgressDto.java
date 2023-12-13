package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto;

import lombok.Data;

@Data
public class CollegeProgressDto {


    private Long collegeId;
    private String collegeName;
    private Long num;
    private Long total;
    private Double ratio;

}
