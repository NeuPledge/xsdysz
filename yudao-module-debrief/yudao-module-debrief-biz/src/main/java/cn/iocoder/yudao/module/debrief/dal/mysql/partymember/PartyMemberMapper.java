package cn.iocoder.yudao.module.debrief.dal.mysql.partymember;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.debrief.dal.dataobject.partymember.PartyMemberDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.debrief.controller.admin.partymember.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 党员信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PartyMemberMapper extends BaseMapperX<PartyMemberDO> {

    default PageResult<PartyMemberDO> selectPage(PartyMemberPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PartyMemberDO>()
                .eqIfPresent(PartyMemberDO::getDebriefStudentId, reqVO.getDebriefStudentId())
                .likeIfPresent(PartyMemberDO::getName, reqVO.getName())
                .eqIfPresent(PartyMemberDO::getStudentNumber, reqVO.getStudentNumber())
                .eqIfPresent(PartyMemberDO::getBranchId, reqVO.getBranchId())
                .eqIfPresent(PartyMemberDO::getCollegeId, reqVO.getCollegeId())
                .eqIfPresent(PartyMemberDO::getGradeId, reqVO.getGradeId())
                .eqIfPresent(PartyMemberDO::getSatisfaction, reqVO.getSatisfaction())
                .eqIfPresent(PartyMemberDO::getBaseSatisfaction, reqVO.getBaseSatisfaction())
                .eqIfPresent(PartyMemberDO::getDissatisfaction, reqVO.getDissatisfaction())
                .betweenIfPresent(PartyMemberDO::getModifyTime, reqVO.getModifyTime())
                .betweenIfPresent(PartyMemberDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PartyMemberDO::getId));
    }
    List<Map<String, Object>> selectCollegeCommentByCollegeIdSharding(Long collegeId);

    List<PartyMemberDO> selectUnequalGradeId(@Param("gradeId") Long gradeId, @Param("size") Long size);


}