package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto;

import lombok.Data;

@Data
public class BranchProgressDto {


    private Long branchId;
    private String branchName;
    private Long num;
    private Long total;
    private Double ratio;

}
