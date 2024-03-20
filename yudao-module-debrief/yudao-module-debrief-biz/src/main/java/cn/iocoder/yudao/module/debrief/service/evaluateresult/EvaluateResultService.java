package cn.iocoder.yudao.module.debrief.service.evaluateresult;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.BranchProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.CollegeProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.UnCommentStudent;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.BranchProgressRespVo;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.CommentTrendRespVO;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.EvaluateResultPageReqVO;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.EvaluateResultSaveReqVO;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.GradeProgressRespVo;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.ProgressTrendRespVO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.evaluateresult.EvaluateResultDO;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 评价结果 Service 接口
 *
 * @author 芋道源码
 */
public interface EvaluateResultService {

    /**
     * 创建评价结果
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createEvaluateResult(@Valid EvaluateResultSaveReqVO createReqVO);

    /**
     * 更新评价结果
     *
     * @param updateReqVO 更新信息
     */
    void updateEvaluateResult(@Valid EvaluateResultSaveReqVO updateReqVO);

    /**
     * 删除评价结果
     *
     * @param id 编号
     */
    void deleteEvaluateResult(Long id);

    /**
     * 获得评价结果
     *
     * @param id 编号
     * @return 评价结果
     */
    EvaluateResultDO getEvaluateResult(Long id);

    /**
     * 获得评价结果分页
     *
     * @param pageReqVO 分页查询
     * @return 评价结果分页
     */
    PageResult<EvaluateResultDO> getEvaluateResultPage(EvaluateResultPageReqVO pageReqVO);

    ProgressTrendRespVO finishProgressTrend();

    CommentTrendRespVO commentTrend();

    List<CollegeProgressDto> collegeProgress();

    BranchProgressRespVo branchProgress();

    GradeProgressRespVo gradeProgress();

    List<UnCommentStudent> uncommentStudent(Long collegeId);

    void analysisReport(List<Long> collegeIds, HttpServletResponse response) throws IOException;

}