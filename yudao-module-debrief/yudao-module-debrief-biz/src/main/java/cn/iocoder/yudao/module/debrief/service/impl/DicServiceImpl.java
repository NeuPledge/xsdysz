package cn.iocoder.yudao.module.debrief.service.impl;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.debrief.controller.app.response.DicCollegeResponse;
import cn.iocoder.yudao.module.debrief.controller.app.response.DicGradeResponse;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade.DicGradeDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicbranch.DicBranchMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.diccollege.DicCollegeMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicgrade.DicGradeMapper;
import cn.iocoder.yudao.module.debrief.service.DicService;
import cn.iocoder.yudao.module.debrief.util.DebriefConverter;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

// @Service
public class DicServiceImpl implements DicService {

    @Resource
    DicCollegeMapper dicCollegeMapper;
    @Resource
    DicGradeMapper dicGradeMapper;
    @Resource
    DicBranchMapper dicBranchMapper;

    @Override
    public String getBranchName(Long branchId) {
        return null;
    }

    @Override
    public List<DicCollegeResponse> getColleges() {
        List<DicCollegeDO> dicColleges = dicCollegeMapper.selectList();
        List<DicCollegeResponse> dicCollegeResponses = DebriefConverter.convertToDtoList(dicColleges, DicCollegeResponse.class);
        return dicCollegeResponses;
    }

    @Override
    public List<DicGradeResponse> getGrades(Long collegeId) {
        List<DicGradeDO> dicGrades = dicGradeMapper.selectList(
                new LambdaQueryWrapperX<DicGradeDO>().eq(DicGradeDO::getCollegeId, collegeId));
        List<DicGradeResponse> dicGradeResponses = DebriefConverter.convertToDtoList(dicGrades, DicGradeResponse.class);
        return dicGradeResponses;
    }

    @Override
    public String getGrade(Long id) {
        DicGradeDO grade = dicGradeMapper.selectById(id);
        if (Objects.isNull(grade)) {
            throw new ServiceException("未找到对应班级");
        }
        return grade.getGradeName();
    }

}
