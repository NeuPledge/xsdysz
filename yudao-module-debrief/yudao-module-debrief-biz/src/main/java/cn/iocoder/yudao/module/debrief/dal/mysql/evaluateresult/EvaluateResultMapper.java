package cn.iocoder.yudao.module.debrief.dal.mysql.evaluateresult;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.BranchProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.CollegeProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.CommentTrendDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.CtypeResult;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.GradeProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.ProgressTrendDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.UnCommentStudent;
import cn.iocoder.yudao.module.debrief.dal.dataobject.evaluateresult.EvaluateResultDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 评价结果 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface EvaluateResultMapper extends BaseMapperX<EvaluateResultDO> {

    default PageResult<EvaluateResultDO> selectPage(EvaluateResultPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<EvaluateResultDO>()
                .eqIfPresent(EvaluateResultDO::getPartyMemberId, reqVO.getPartyMemberId())
                .eqIfPresent(EvaluateResultDO::getCommenterId, reqVO.getCommenterId())
                .eqIfPresent(EvaluateResultDO::getComment, reqVO.getComment())
                .eqIfPresent(EvaluateResultDO::getBranchId, reqVO.getBranchId())
                .eqIfPresent(EvaluateResultDO::getCollegeId, reqVO.getCollegeId())
                .eqIfPresent(EvaluateResultDO::getGradeId, reqVO.getGradeId())
                .eqIfPresent(EvaluateResultDO::getContent, reqVO.getContent())
                .betweenIfPresent(EvaluateResultDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(EvaluateResultDO::getId));
    }

    List<Map<String, Long>> groupByCount(@Param("comment") Integer comment, @Param("collegeId") Long collegeId);


    List<ProgressTrendDto> finishProgressTrend(@Param("startDate") String startDate);

    List<CommentTrendDto> commentTrend(@Param("startDate") String startDate);

    List<UnCommentStudent> unCommentStudent(@Param("collegeId") Long collegeId);

    List<CollegeProgressDto> collegeProgress();

    List<BranchProgressDto> branchProgress();

    List<GradeProgressDto> gradeProgress();


    List<CtypeResult> c1Result(@Param("collegeId") Long collegeId, @Param("ctype") String ctype);
}