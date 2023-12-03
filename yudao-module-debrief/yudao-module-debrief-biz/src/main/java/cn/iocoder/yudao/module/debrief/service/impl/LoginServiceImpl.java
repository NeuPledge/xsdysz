package cn.iocoder.yudao.module.debrief.service.impl;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.debrief.controller.app.request.CheckRequest;
import cn.iocoder.yudao.module.debrief.controller.app.request.LoginRequest;
import cn.iocoder.yudao.module.debrief.controller.app.response.PartyMemberResponse;
import cn.iocoder.yudao.module.debrief.dal.dataobject.partymember.PartyMemberDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.partymember.PartyMemberMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.student.StudentMapper;
import cn.iocoder.yudao.module.debrief.service.LoginService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    StudentMapper studentMapper;

    @Resource
    PartyMemberMapper partyMemberMapper;

    @Override
    public Long login(LoginRequest request) {
        if (Objects.isNull(request.getName()) || Objects.equals(request.getName(), "")) {
            throw new ServiceException("请输入姓名");
        }
        if (Objects.isNull(request.getStudentNumber()) || Objects.equals(request.getStudentNumber(), "")) {
            throw new ServiceException("请输入学号");
        }
        StudentDO student = new StudentDO();
        student.setCollegeId(request.getCollegeId());
        student.setGradeId(request.getGradeId());
        student.setName(request.getName());
        student.setStudentNumber(request.getStudentNumber());
        //todo 需要在sql里面添加 班级 验证
        StudentDO result = studentMapper.selectOne(
                new LambdaQueryWrapperX<StudentDO>()
                        .eq(StudentDO::getCollegeId, student.getCollegeId())
                        .eq(StudentDO::getName, student.getName())
                        .eq(StudentDO::getStudentNumber, student.getStudentNumber())
        );
        if (Objects.isNull(result)) {
            throw new ServiceException("未找到您的信息");
        }
        Long gradeId = request.getGradeId();
        List<PartyMemberDO> partyMembers = partyMemberMapper.selectList(new LambdaQueryWrapperX<PartyMemberDO>().eq(PartyMemberDO::getGradeId, gradeId));
        if (CollectionUtils.isEmpty(partyMembers)) {
            throw new ServiceException("未查询到您班级的党员数据");
        }
        return result.getId();
    }

    @Override
    public List<PartyMemberResponse> getRandom(Long gradeId) {
        //返回5个人
        List<PartyMemberResponse> responses = new ArrayList<>();
        List<PartyMemberDO> partyMembers = partyMemberMapper.selectList(new LambdaQueryWrapperX<PartyMemberDO>().eq(PartyMemberDO::getGradeId, gradeId));
        int partySizes;
        //如果班级党员数大于三个 选3个出来
        //3个gradeId对应班级的党员
        if (partyMembers.size() > 3) {
            partySizes = 3;
            List<PartyMemberDO> randomList = cn.iocoder.yudao.module.debrief.util.CollectionUtils.createRandomList(partyMembers, partySizes);
            randomList.forEach(partyMember -> {
                PartyMemberResponse response = new PartyMemberResponse();
                response.setId(partyMember.getId());
                response.setName(partyMember.getName());
                responses.add(response);
            });
        } else {
            partySizes = partyMembers.size();
            partyMembers.forEach(partyMember -> {
                PartyMemberResponse response = new PartyMemberResponse();
                response.setId(partyMember.getId());
                response.setName(partyMember.getName());
                responses.add(response);
            });
        }
        //剩下都选非该班级党员
        Long lastSize = Long.valueOf(5 - partySizes);
        List<PartyMemberDO> otherGradePartyMembers = partyMemberMapper.selectUnequalGradeId(gradeId, lastSize);
        otherGradePartyMembers.forEach(partyMember -> {
            PartyMemberResponse response = new PartyMemberResponse();
            response.setId(partyMember.getId());
            response.setName(partyMember.getName());
            responses.add(response);
        });
        if (responses.size() != 5) {
            throw new ServiceException("系统错误，请重试");
        }
        return responses;
    }

    @Override
    public Boolean check(CheckRequest checkRequest) {
        List<Long> ids = checkRequest.getIds();
        ids.forEach(id -> {
            PartyMemberDO partyMember = partyMemberMapper.selectById(id);
            if (partyMember.getGradeId() != checkRequest.getGradeId()) {
                throw new ServiceException("验证失败");
            }
        });
        return true;
    }
}
