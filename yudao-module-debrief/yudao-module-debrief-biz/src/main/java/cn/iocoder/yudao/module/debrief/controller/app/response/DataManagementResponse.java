package cn.iocoder.yudao.module.debrief.controller.app.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataManagementResponse {

    // 是否是党员
    private Boolean isMember;

    // 党员id
    private Long id;

    // 学生id
    private Long debriefStudentId;

    // 学生姓名
    private String name;

    private String studentNumber;

    private Long branchId;
    private String branchName;

    private Long collegeId;
    private String collegeName;

    private Long gradeId;
    private String gradeName;


}
