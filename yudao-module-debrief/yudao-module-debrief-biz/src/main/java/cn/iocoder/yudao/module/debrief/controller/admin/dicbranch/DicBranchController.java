package cn.iocoder.yudao.module.debrief.controller.admin.dicbranch;

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




import cn.iocoder.yudao.module.debrief.controller.admin.dicbranch.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicbranch.DicBranchDO;
import cn.iocoder.yudao.module.debrief.service.dicbranch.DicBranchService;

@Tag(name = "管理后台 - 支部字典列")
@RestController
@RequestMapping("/debrief/dic-branch")
@Validated
public class DicBranchController {

    @Resource
    private DicBranchService dicBranchService;

    @PostMapping("/create")
    @Operation(summary = "创建支部字典列")
    @PreAuthorize("@ss.hasPermission('debrief:dic-branch:create')")
    public CommonResult<Long> createDicBranch(@Valid @RequestBody DicBranchSaveReqVO createReqVO) {
        return success(dicBranchService.createDicBranch(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新支部字典列")
    @PreAuthorize("@ss.hasPermission('debrief:dic-branch:update')")
    public CommonResult<Boolean> updateDicBranch(@Valid @RequestBody DicBranchSaveReqVO updateReqVO) {
        dicBranchService.updateDicBranch(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除支部字典列")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('debrief:dic-branch:delete')")
    public CommonResult<Boolean> deleteDicBranch(@RequestParam("id") Long id) {
        dicBranchService.deleteDicBranch(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得支部字典列")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('debrief:dic-branch:query')")
    public CommonResult<DicBranchRespVO> getDicBranch(@RequestParam("id") Long id) {
        DicBranchDO dicBranch = dicBranchService.getDicBranch(id);
        return success(BeanUtils.toBean(dicBranch, DicBranchRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得支部字典列分页")
    @PreAuthorize("@ss.hasPermission('debrief:dic-branch:query')")
    public CommonResult<PageResult<DicBranchRespVO>> getDicBranchPage(@Valid DicBranchPageReqVO pageReqVO) {
        PageResult<DicBranchDO> pageResult = dicBranchService.getDicBranchPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DicBranchRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出支部字典列 Excel")
    @PreAuthorize("@ss.hasPermission('debrief:dic-branch:export')")
    
    public void exportDicBranchExcel(@Valid DicBranchPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DicBranchDO> list = dicBranchService.getDicBranchPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "支部字典列.xls", "数据", DicBranchRespVO.class,
                        BeanUtils.toBean(list, DicBranchRespVO.class));
    }

}