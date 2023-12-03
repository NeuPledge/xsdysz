package cn.iocoder.yudao.module.debrief.service.impl;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.debrief.controller.app.request.SubmitCommentRequest;
import cn.iocoder.yudao.module.debrief.controller.app.request.vo.Comment;
import cn.iocoder.yudao.module.debrief.controller.app.response.ListResponse;
import cn.iocoder.yudao.module.debrief.controller.app.response.PartyMemberResponse;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade.DicGradeDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.evaluateresult.EvaluateResultDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.partymember.PartyMemberDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicgrade.DicGradeMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.evaluateresult.EvaluateResultMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.partymember.PartyMemberMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.student.StudentMapper;
import cn.iocoder.yudao.module.debrief.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ListServiceImpl implements ListService {

    @Resource
    StudentMapper studentMapper;

    @Resource
    PartyMemberMapper partyMemberMapper;

    @Resource
    EvaluateResultMapper evaluateResultMapper;

    @Resource
    DicGradeMapper dicGradeMapper;


    @Override
    public ListResponse getPartyMemberByGradeIdFilter(Long gradeId, String studentNumner) {
        //按照学号来过滤掉自己
        if (StringUtils.isEmpty(studentNumner)) {
            throw new ServiceException("当前并发较高，请稍后再试");
        }
        DicGradeDO dicGrade = dicGradeMapper.selectById(gradeId);
        if (Objects.isNull(dicGrade)) {
            throw new ServiceException("未查询到对应班级");
        }
        List<PartyMemberDO> partyMembers = partyMemberMapper.selectList(new LambdaQueryWrapperX<PartyMemberDO>().eq(PartyMemberDO::getGradeId, gradeId));
        List<PartyMemberResponse> responses = new ArrayList<>();
        for (PartyMemberDO partyMember : partyMembers) {
            if (!partyMember.getStudentNumber().equals(studentNumner)) {
                // if (!Objects.equals(partyMember.getStudentNumber(), studentNumner)) {
                PartyMemberResponse response = new PartyMemberResponse();
                response.setId(partyMember.getId());
                response.setName(partyMember.getName());
                responses.add(response);
            }
        }
        ListResponse listResponse = new ListResponse();
        listResponse.setResponses(responses);
        listResponse.setGradeName(dicGrade.getGradeName());
        return listResponse;
    }

    @Override
    public Boolean submitComment(SubmitCommentRequest submit) {
        Long commenterId = submit.getId();//提交者id
        List<Comment> comments = submit.getComments();
//        debrief_evaluate_result
        for (Comment comment : comments) {
            //首先判断是否已经评价过该党员
            Long partyMemberId = comment.getId();//党员id
            Long commentTypeId = comment.getCommentTypeId();
            String content = comment.getContent();
            EvaluateResultDO result = evaluateResultMapper
                    .selectOne(new LambdaQueryWrapperX<EvaluateResultDO>()
                            .eq(EvaluateResultDO::getPartyMemberId, partyMemberId)
                            .eq(EvaluateResultDO::getCommenterId, commenterId)
                    );
            if (Objects.nonNull(result)) {
                //TODO 不做任何操作？  or  更新记录？
                continue;
            }
            PartyMemberDO partyMember = partyMemberMapper.selectById(partyMemberId);
            EvaluateResultDO evaluateResult = new EvaluateResultDO();
            evaluateResult.setPartyMemberId(partyMemberId);
            evaluateResult.setCommenterId(commenterId);
            evaluateResult.setComment(commentTypeId);
            evaluateResult.setContent(content);
            evaluateResult.setCollegeId(partyMember.getCollegeId());
            evaluateResult.setGradeId(partyMember.getGradeId());
            evaluateResult.setBranchId(partyMember.getBranchId());
            evaluateResultMapper.insert(evaluateResult);
        }
        return true;
    }
}
