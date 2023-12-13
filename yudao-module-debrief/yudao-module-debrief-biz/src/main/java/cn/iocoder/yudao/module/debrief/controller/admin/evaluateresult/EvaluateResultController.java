package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult;

import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.CollegeProgressDto;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.ProgressTrendDto;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;

import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.evaluateresult.EvaluateResultDO;
import cn.iocoder.yudao.module.debrief.service.evaluateresult.EvaluateResultService;

@Tag(name = "管理后台 - 评价结果")
@RestController
@RequestMapping("/debrief/evaluate-result")
@Validated
public class EvaluateResultController {

    @Resource
    private EvaluateResultService evaluateResultService;

    @PostMapping("/create")
    @Operation(summary = "创建评价结果")
    @PreAuthorize("@ss.hasPermission('debrief:evaluate-result:create')")
    public CommonResult<Long> createEvaluateResult(@Valid @RequestBody EvaluateResultSaveReqVO createReqVO) {
        return success(evaluateResultService.createEvaluateResult(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新评价结果")
    @PreAuthorize("@ss.hasPermission('debrief:evaluate-result:update')")
    public CommonResult<Boolean> updateEvaluateResult(@Valid @RequestBody EvaluateResultSaveReqVO updateReqVO) {
        evaluateResultService.updateEvaluateResult(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除评价结果")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('debrief:evaluate-result:delete')")
    public CommonResult<Boolean> deleteEvaluateResult(@RequestParam("id") Long id) {
        evaluateResultService.deleteEvaluateResult(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得评价结果")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('debrief:evaluate-result:query')")
    public CommonResult<EvaluateResultRespVO> getEvaluateResult(@RequestParam("id") Long id) {
        EvaluateResultDO evaluateResult = evaluateResultService.getEvaluateResult(id);
        return success(BeanUtils.toBean(evaluateResult, EvaluateResultRespVO.class));
    }

    @GetMapping("/finish-progress-trend")
    @Operation(summary = "近30日测评完成党员进度趋势")
    public CommonResult<ProgressTrendRespVO> finishProgressTrend() {
        ProgressTrendRespVO vo = evaluateResultService.finishProgressTrend();
        return success(vo);
    }

    @GetMapping("/comment-trend")
    @Operation(summary = "近30日测评票数趋势")
    public CommonResult<CommentTrendRespVO> commentTrend() {
        CommentTrendRespVO vo = evaluateResultService.commentTrend();
        return success(vo);
    }

    @GetMapping("/college-progress")
    @Operation(summary = "学院进度")
    public CommonResult<List<CollegeProgressDto>> collegeProgress() {
        List<CollegeProgressDto> collegeProgressDtos = evaluateResultService.collegeProgress();
        return success(collegeProgressDtos);
    }

    @GetMapping("/branch-progress")
    @Operation(summary = "支部进度")
    public CommonResult<BranchProgressRespVo> branchProgress() {
        BranchProgressRespVo branchProgressRespVo = evaluateResultService.branchProgress();
        return success(branchProgressRespVo);
    }

    @GetMapping("/grade-progress")
    @Operation(summary = "班级进度")
    public CommonResult<GradeProgressRespVo> gradeProgress() {
        GradeProgressRespVo gradeProgress = evaluateResultService.gradeProgress();
        return success(gradeProgress);
    }

    @GetMapping("/page")
    @Operation(summary = "获得评价结果分页")
    @PreAuthorize("@ss.hasPermission('debrief:evaluate-result:query')")
    public CommonResult<PageResult<EvaluateResultRespVO>> getEvaluateResultPage(@Valid EvaluateResultPageReqVO pageReqVO) {
        PageResult<EvaluateResultDO> pageResult = evaluateResultService.getEvaluateResultPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, EvaluateResultRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出评价结果 Excel")
    @PreAuthorize("@ss.hasPermission('debrief:evaluate-result:export')")
    @OperateLog(type = EXPORT)
    public void exportEvaluateResultExcel(@Valid EvaluateResultPageReqVO pageReqVO,
                                          HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<EvaluateResultDO> list = evaluateResultService.getEvaluateResultPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "评价结果.xls", "数据", EvaluateResultRespVO.class,
                BeanUtils.toBean(list, EvaluateResultRespVO.class));
    }

}