package cn.iocoder.yudao.module.debrief.controller.admin.dicgrade;

import cn.iocoder.yudao.module.debrief.controller.app.response.DicCollegeResponse;
import cn.iocoder.yudao.module.debrief.service.DicService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;




import cn.iocoder.yudao.module.debrief.controller.admin.dicgrade.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade.DicGradeDO;
import cn.iocoder.yudao.module.debrief.service.dicgrade.DicGradeService;

@Tag(name = "管理后台 - 班级字典列")
@RestController
@RequestMapping("/debrief/dic-grade")
@Validated
public class DicGradeController {

    @Resource
    private DicGradeService dicGradeService;

    @Resource
    private DicService dicService;

    @PostMapping("/create")
    @Operation(summary = "创建班级字典列")
    @PreAuthorize("@ss.hasPermission('debrief:dic-grade:create')")
    public CommonResult<Long> createDicGrade(@Valid @RequestBody DicGradeSaveReqVO createReqVO) {
        return success(dicGradeService.createDicGrade(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新班级字典列")
    @PreAuthorize("@ss.hasPermission('debrief:dic-grade:update')")
    public CommonResult<Boolean> updateDicGrade(@Valid @RequestBody DicGradeSaveReqVO updateReqVO) {
        dicGradeService.updateDicGrade(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除班级字典列")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('debrief:dic-grade:delete')")
    public CommonResult<Boolean> deleteDicGrade(@RequestParam("id") Long id) {
        dicGradeService.deleteDicGrade(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得班级字典列")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('debrief:dic-grade:query')")
    public CommonResult<DicGradeRespVO> getDicGrade(@RequestParam("id") Long id) {
        DicGradeDO dicGrade = dicGradeService.getDicGrade(id);
        return success(BeanUtils.toBean(dicGrade, DicGradeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得班级字典列分页")
    @PreAuthorize("@ss.hasPermission('debrief:dic-grade:query')")
    public CommonResult<PageResult<DicGradeRespVO>> getDicGradePage(@Valid DicGradePageReqVO pageReqVO) {
        PageResult<DicGradeDO> pageResult = dicGradeService.getDicGradePage(pageReqVO);
        PageResult<DicGradeRespVO> dicGradeRespVOPageResult = BeanUtils.toBean(pageResult, DicGradeRespVO.class);
        List<DicCollegeResponse> colleges = dicService.getColleges();
        Map<Long, String> collegeMap = colleges.stream().collect(Collectors.toMap(e -> e.getId(), e -> e.getCollegeName()));
        for (DicGradeRespVO dicGradeRespVO : dicGradeRespVOPageResult.getList()) {
            Long collegeId = dicGradeRespVO.getCollegeId();
            String name = collegeMap.get(collegeId);
            dicGradeRespVO.setCollegeName(name);
        }

        return success(dicGradeRespVOPageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出班级字典列 Excel")
    @PreAuthorize("@ss.hasPermission('debrief:dic-grade:export')")
    
    public void exportDicGradeExcel(@Valid DicGradePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DicGradeDO> list = dicGradeService.getDicGradePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "班级字典列.xls", "数据", DicGradeRespVO.class,
                        BeanUtils.toBean(list, DicGradeRespVO.class));
    }

}