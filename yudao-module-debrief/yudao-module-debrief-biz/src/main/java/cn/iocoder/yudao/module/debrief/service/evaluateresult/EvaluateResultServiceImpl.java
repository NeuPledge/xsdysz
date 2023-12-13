package cn.iocoder.yudao.module.debrief.service.evaluateresult;

import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.BranchProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.CollegeProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.CommentTrendDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.GradeProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.ProgressTrendDto;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicbranch.DicBranchMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicgrade.DicGradeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.evaluateresult.EvaluateResultDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.debrief.dal.mysql.evaluateresult.EvaluateResultMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.debrief.enums.ErrorCodeConstants.*;

/**
 * 评价结果 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class EvaluateResultServiceImpl implements EvaluateResultService {

    @Resource
    private EvaluateResultMapper evaluateResultMapper;

    @Resource
    private DicBranchMapper dicBranchMapper;

    @Resource
    private DicGradeMapper dicGradeMapper;

    @Override
    public Long createEvaluateResult(EvaluateResultSaveReqVO createReqVO) {
        // 插入
        EvaluateResultDO evaluateResult = BeanUtils.toBean(createReqVO, EvaluateResultDO.class);
        evaluateResultMapper.insert(evaluateResult);
        // 返回
        return evaluateResult.getId();
    }

    @Override
    public void updateEvaluateResult(EvaluateResultSaveReqVO updateReqVO) {
        // 校验存在
        validateEvaluateResultExists(updateReqVO.getId());
        // 更新
        EvaluateResultDO updateObj = BeanUtils.toBean(updateReqVO, EvaluateResultDO.class);
        evaluateResultMapper.updateById(updateObj);
    }

    @Override
    public void deleteEvaluateResult(Long id) {
        // 校验存在
        validateEvaluateResultExists(id);
        // 删除
        evaluateResultMapper.deleteById(id);
    }

    private void validateEvaluateResultExists(Long id) {
        if (evaluateResultMapper.selectById(id) == null) {
            throw exception(EVALUATE_RESULT_NOT_EXISTS);
        }
    }

    @Override
    public EvaluateResultDO getEvaluateResult(Long id) {
        return evaluateResultMapper.selectById(id);
    }

    @Override
    public PageResult<EvaluateResultDO> getEvaluateResultPage(EvaluateResultPageReqVO pageReqVO) {
        return evaluateResultMapper.selectPage(pageReqVO);
    }


    @Override
    public ProgressTrendRespVO finishProgressTrend() {
        List<ProgressTrendDto> progressTrendDtos = evaluateResultMapper.finishProgressTrend();

        ProgressTrendRespVO progressTrendRespVO = new ProgressTrendRespVO();
        List<String> days = new ArrayList<>();
        List<Long> dayCounts = new ArrayList<>();
        List<Long> totalCounts = new ArrayList<>();
        for (ProgressTrendDto progressTrendDto : progressTrendDtos) {
            days.add(progressTrendDto.getDay());
            dayCounts.add(progressTrendDto.getDayCount());
            totalCounts.add(progressTrendDto.getTotalCount());
        }
        progressTrendRespVO.setDays(days);
        progressTrendRespVO.setDayCounts(dayCounts);
        progressTrendRespVO.setTotalCounts(totalCounts);

        return progressTrendRespVO;
    }

    @Override
    public CommentTrendRespVO commentTrend() {
        List<CommentTrendDto> commentTrendDtos = evaluateResultMapper.commentTrend();
        Map<String, List<CommentTrendDto>> dayMap = commentTrendDtos.stream().collect(Collectors.groupingBy(e -> e.getDay(), Collectors.toList()));
        List<String> days = new ArrayList<>();
        List<Long> comment1List = new ArrayList<>();
        List<Long> comment2List = new ArrayList<>();
        List<Long> comment3List = new ArrayList<>();
        for (String day : dayMap.keySet()) {
            List<CommentTrendDto> comments = dayMap.get(day);
            long comment1 = 0;
            long comment2 = 0;
            long comment3 = 0;
            for (CommentTrendDto comment : comments) {
                if (comment.getComment() == 1) {
                    comment1 = comment.getNum();
                } else if (comment.getComment() == 2) {
                    comment2 = comment.getNum();
                } else if (comment.getComment() == 3) {
                    comment3 = comment.getNum();
                }
            }
            days.add(day);
            comment1List.add(comment1);
            comment2List.add(comment2);
            comment3List.add(comment3);
        }
        CommentTrendRespVO commentTrendRespVO = new CommentTrendRespVO();
        commentTrendRespVO.setDays(days);
        commentTrendRespVO.setComment1(comment1List);
        commentTrendRespVO.setComment2(comment2List);
        commentTrendRespVO.setComment3(comment3List);
        return commentTrendRespVO;
    }

    @Override
    public List<CollegeProgressDto> collegeProgress() {
        List<CollegeProgressDto> collegeProgressDtos = evaluateResultMapper.collegeProgress();
        return collegeProgressDtos;
    }

    @Override
    public BranchProgressRespVo branchProgress() {

        BranchProgressRespVo branchProgressRespVo = new BranchProgressRespVo();

        List<BranchProgressDto> branchProgress = evaluateResultMapper.branchProgress();
        List<BranchProgressDto> list = new ArrayList<>();
        int i = 1;

        Long branchCount = dicBranchMapper.selectCount();
        int finishCount = 0;
        for (BranchProgressDto progress : branchProgress) {
            if (progress.getNum() >= progress.getTotal()) {
                finishCount++;
            }
            if (i <= 20) {
                list.add(progress);
            }
            i++;

        }
        branchProgressRespVo.setList(list);
        branchProgressRespVo.setProgress(finishCount + "/" + branchCount);

        return branchProgressRespVo;
    }

    @Override
    public GradeProgressRespVo gradeProgress() {

        GradeProgressRespVo gradeProgressRespVo = new GradeProgressRespVo();

        List<GradeProgressDto> progressDtos = evaluateResultMapper.gradeProgress();

        List<GradeProgressDto> list = new ArrayList<>();
        int i = 1;

        Long branchCount = dicGradeMapper.selectCount();
        int finishCount = 0;
        for (GradeProgressDto progress : progressDtos) {
            if (progress.getNum() >= progress.getTotal()) {
                finishCount++;
            }
            if (i <= 40) {
                list.add(progress);
            }
            i++;

        }
        gradeProgressRespVo.setList(list);

        gradeProgressRespVo.setProgress(finishCount + "/" + branchCount);

        return gradeProgressRespVo;
    }
}