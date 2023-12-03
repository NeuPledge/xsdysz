package cn.iocoder.yudao.module.debrief.controller.app.response;

import lombok.Data;

import java.util.List;

@Data
public class ListResponse {
    private String gradeName;
    private List<PartyMemberResponse> responses;
}
