package cn.iocoder.yudao.module.debrief.controller.app.request;

import lombok.Data;

@Data
public class LoginRequest {
    private Long collegeId;
    private Long gradeId;
    private String name;
    private String studentNumber;
}
