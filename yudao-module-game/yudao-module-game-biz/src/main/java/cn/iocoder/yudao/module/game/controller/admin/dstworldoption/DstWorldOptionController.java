package cn.iocoder.yudao.module.game.controller.admin.dstworldoption;

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




import cn.iocoder.yudao.module.game.controller.admin.dstworldoption.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.dstworldoption.DstWorldOptionDO;
import cn.iocoder.yudao.module.game.service.dstworldoption.DstWorldOptionService;

@Tag(name = "管理后台 - 饥荒世界配置初始化数据")
@RestController
@RequestMapping("/game/dst-world-option")
@Validated
public class DstWorldOptionController {

    @Resource
    private DstWorldOptionService dstWorldOptionService;

    @PostMapping("/create")
    @Operation(summary = "创建饥荒世界配置初始化数据")
    @PreAuthorize("@ss.hasPermission('game:dst-world-option:create')")
    public CommonResult<Long> createDstWorldOption(@Valid @RequestBody DstWorldOptionSaveReqVO createReqVO) {
        return success(dstWorldOptionService.createDstWorldOption(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新饥荒世界配置初始化数据")
    @PreAuthorize("@ss.hasPermission('game:dst-world-option:update')")
    public CommonResult<Boolean> updateDstWorldOption(@Valid @RequestBody DstWorldOptionSaveReqVO updateReqVO) {
        dstWorldOptionService.updateDstWorldOption(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除饥荒世界配置初始化数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('game:dst-world-option:delete')")
    public CommonResult<Boolean> deleteDstWorldOption(@RequestParam("id") Long id) {
        dstWorldOptionService.deleteDstWorldOption(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得饥荒世界配置初始化数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('game:dst-world-option:query')")
    public CommonResult<DstWorldOptionRespVO> getDstWorldOption(@RequestParam("id") Long id) {
        DstWorldOptionDO dstWorldOption = dstWorldOptionService.getDstWorldOption(id);
        return success(BeanUtils.toBean(dstWorldOption, DstWorldOptionRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得饥荒世界配置初始化数据分页")
    @PreAuthorize("@ss.hasPermission('game:dst-world-option:query')")
    public CommonResult<PageResult<DstWorldOptionRespVO>> getDstWorldOptionPage(@Valid DstWorldOptionPageReqVO pageReqVO) {
        PageResult<DstWorldOptionDO> pageResult = dstWorldOptionService.getDstWorldOptionPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DstWorldOptionRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出饥荒世界配置初始化数据 Excel")
    @PreAuthorize("@ss.hasPermission('game:dst-world-option:export')")
    
    public void exportDstWorldOptionExcel(@Valid DstWorldOptionPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DstWorldOptionDO> list = dstWorldOptionService.getDstWorldOptionPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "饥荒世界配置初始化数据.xls", "数据", DstWorldOptionRespVO.class,
                        BeanUtils.toBean(list, DstWorldOptionRespVO.class));
    }

}