package cn.iocoder.yudao.module.debrief.controller.app.vo;

import lombok.Data;

@Data
public class PartyMemberCommentData {
    private Long id;

    private String name;

    private String studentNumber;

    private String branchName;

    private String collegeName;

    private String gradeName;

    private String content;
}
