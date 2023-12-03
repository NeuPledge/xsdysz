package cn.iocoder.yudao.module.debrief.service;


import cn.iocoder.yudao.module.debrief.controller.app.request.SubmitCommentRequest;
import cn.iocoder.yudao.module.debrief.controller.app.response.ListResponse;

public interface ListService {

    Boolean submitComment(SubmitCommentRequest submit);

    ListResponse getPartyMemberByGradeIdFilter(Long gradeId, String studentNumner);
}
