package cn.iocoder.yudao.module.debrief.controller.app.vo;

import lombok.Data;

@Data
public class PartyMemberResultData {
    private Long id;

    private String name;

    private String studentNumber;

    private String branchName;

    private String collegeName;

    private String gradeName;

    private Long satisfaction;

    private Long baseSatisfaction;

    private Long dissatisfaction;

}
