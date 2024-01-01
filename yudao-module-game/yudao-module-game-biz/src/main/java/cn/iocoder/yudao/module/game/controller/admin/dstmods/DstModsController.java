package cn.iocoder.yudao.module.game.controller.admin.dstmods;

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

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.game.controller.admin.dstmods.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.dstmods.DstModsDO;
import cn.iocoder.yudao.module.game.service.dstmods.DstModsService;

@Tag(name = "管理后台 - 饥荒模组信息")
@RestController
@RequestMapping("/game/dst-mods")
@Validated
public class DstModsController {

    @Resource
    private DstModsService dstModsService;

    @PostMapping("/create")
    @Operation(summary = "创建饥荒模组信息")
    @PreAuthorize("@ss.hasPermission('game:dst-mods:create')")
    public CommonResult<Long> createDstMods(@Valid @RequestBody DstModsSaveReqVO createReqVO) {
        return success(dstModsService.createDstMods(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新饥荒模组信息")
    @PreAuthorize("@ss.hasPermission('game:dst-mods:update')")
    public CommonResult<Boolean> updateDstMods(@Valid @RequestBody DstModsSaveReqVO updateReqVO) {
        dstModsService.updateDstMods(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除饥荒模组信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('game:dst-mods:delete')")
    public CommonResult<Boolean> deleteDstMods(@RequestParam("id") Long id) {
        dstModsService.deleteDstMods(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得饥荒模组信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('game:dst-mods:query')")
    public CommonResult<DstModsRespVO> getDstMods(@RequestParam("id") Long id) {
        DstModsDO dstMods = dstModsService.getDstMods(id);
        return success(BeanUtils.toBean(dstMods, DstModsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得饥荒模组信息分页")
    @PreAuthorize("@ss.hasPermission('game:dst-mods:query')")
    public CommonResult<PageResult<DstModsRespVO>> getDstModsPage(@Valid DstModsPageReqVO pageReqVO) {
        PageResult<DstModsDO> pageResult = dstModsService.getDstModsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DstModsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出饥荒模组信息 Excel")
    @PreAuthorize("@ss.hasPermission('game:dst-mods:export')")
    @OperateLog(type = EXPORT)
    public void exportDstModsExcel(@Valid DstModsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DstModsDO> list = dstModsService.getDstModsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "饥荒模组信息.xls", "数据", DstModsRespVO.class,
                        BeanUtils.toBean(list, DstModsRespVO.class));
    }

}