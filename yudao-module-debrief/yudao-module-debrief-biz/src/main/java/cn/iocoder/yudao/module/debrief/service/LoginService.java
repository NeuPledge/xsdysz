package cn.iocoder.yudao.module.debrief.service;


import cn.iocoder.yudao.module.debrief.controller.app.request.CheckRequest;
import cn.iocoder.yudao.module.debrief.controller.app.request.LoginRequest;
import cn.iocoder.yudao.module.debrief.controller.app.response.PartyMemberResponse;

import java.util.List;

public interface LoginService {
    Long login(LoginRequest request);

    List<PartyMemberResponse> getRandom(Long gradeId);

    Boolean check(CheckRequest checkRequest);
}
