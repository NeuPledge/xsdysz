package cn.iocoder.yudao.module.debrief.controller.admin.diccollege;

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

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;




import cn.iocoder.yudao.module.debrief.controller.admin.diccollege.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import cn.iocoder.yudao.module.debrief.service.diccollege.DicCollegeService;

@Tag(name = "管理后台 - 学院字典列")
@RestController
@RequestMapping("/debrief/dic-college")
@Validated
public class DicCollegeController {

    @Resource
    private DicCollegeService dicCollegeService;

    @PostMapping("/create")
    @Operation(summary = "创建学院字典列")
    @PreAuthorize("@ss.hasPermission('debrief:dic-college:create')")
    public CommonResult<Long> createDicCollege(@Valid @RequestBody DicCollegeSaveReqVO createReqVO) {
        return success(dicCollegeService.createDicCollege(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新学院字典列")
    @PreAuthorize("@ss.hasPermission('debrief:dic-college:update')")
    public CommonResult<Boolean> updateDicCollege(@Valid @RequestBody DicCollegeSaveReqVO updateReqVO) {
        dicCollegeService.updateDicCollege(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除学院字典列")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('debrief:dic-college:delete')")
    public CommonResult<Boolean> deleteDicCollege(@RequestParam("id") Long id) {
        dicCollegeService.deleteDicCollege(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得学院字典列")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('debrief:dic-college:query')")
    public CommonResult<DicCollegeRespVO> getDicCollege(@RequestParam("id") Long id) {
        DicCollegeDO dicCollege = dicCollegeService.getDicCollege(id);
        return success(BeanUtils.toBean(dicCollege, DicCollegeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得学院字典列分页")
    @PreAuthorize("@ss.hasPermission('debrief:dic-college:query')")
    public CommonResult<PageResult<DicCollegeRespVO>> getDicCollegePage(@Valid DicCollegePageReqVO pageReqVO) {
        PageResult<DicCollegeDO> pageResult = dicCollegeService.getDicCollegePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DicCollegeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出学院字典列 Excel")
    @PreAuthorize("@ss.hasPermission('debrief:dic-college:export')")
    
    public void exportDicCollegeExcel(@Valid DicCollegePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DicCollegeDO> list = dicCollegeService.getDicCollegePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "学院字典列.xls", "数据", DicCollegeRespVO.class,
                        BeanUtils.toBean(list, DicCollegeRespVO.class));
    }

}