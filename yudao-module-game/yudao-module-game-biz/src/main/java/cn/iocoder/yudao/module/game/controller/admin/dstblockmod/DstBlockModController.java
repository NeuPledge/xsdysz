package cn.iocoder.yudao.module.game.controller.admin.dstblockmod;

import org.springframework.web.bind.annotation.*;
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

import cn.iocoder.yudao.module.game.controller.admin.dstblockmod.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.dstblockmod.DstBlockModDO;
import cn.iocoder.yudao.module.game.service.dstblockmod.DstBlockModService;

import javax.annotation.Resource;

@Tag(name = "管理后台 - 模组黑名单,此名单上的模组,将会警告提示/禁止添加")
@RestController
@RequestMapping("/game/dst-block-mod")
@Validated
public class DstBlockModController {

    @Resource
    private DstBlockModService dstBlockModService;

    @PostMapping("/create")
    @Operation(summary = "创建模组黑名单,此名单上的模组,将会警告提示/禁止添加")
    @PreAuthorize("@ss.hasPermission('game:dst-block-mod:create')")
    public CommonResult<Long> createDstBlockMod(@Valid @RequestBody DstBlockModSaveReqVO createReqVO) {
        return success(dstBlockModService.createDstBlockMod(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新模组黑名单,此名单上的模组,将会警告提示/禁止添加")
    @PreAuthorize("@ss.hasPermission('game:dst-block-mod:update')")
    public CommonResult<Boolean> updateDstBlockMod(@Valid @RequestBody DstBlockModSaveReqVO updateReqVO) {
        dstBlockModService.updateDstBlockMod(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除模组黑名单,此名单上的模组,将会警告提示/禁止添加")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('game:dst-block-mod:delete')")
    public CommonResult<Boolean> deleteDstBlockMod(@RequestParam("id") Long id) {
        dstBlockModService.deleteDstBlockMod(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得模组黑名单,此名单上的模组,将会警告提示/禁止添加")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('game:dst-block-mod:query')")
    public CommonResult<DstBlockModRespVO> getDstBlockMod(@RequestParam("id") Long id) {
        DstBlockModDO dstBlockMod = dstBlockModService.getDstBlockMod(id);
        return success(BeanUtils.toBean(dstBlockMod, DstBlockModRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得模组黑名单,此名单上的模组,将会警告提示/禁止添加分页")
    @PreAuthorize("@ss.hasPermission('game:dst-block-mod:query')")
    public CommonResult<PageResult<DstBlockModRespVO>> getDstBlockModPage(@Valid DstBlockModPageReqVO pageReqVO) {
        PageResult<DstBlockModDO> pageResult = dstBlockModService.getDstBlockModPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DstBlockModRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出模组黑名单,此名单上的模组,将会警告提示/禁止添加 Excel")
    @PreAuthorize("@ss.hasPermission('game:dst-block-mod:export')")
    @OperateLog(type = EXPORT)
    public void exportDstBlockModExcel(@Valid DstBlockModPageReqVO pageReqVO,
                                       HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DstBlockModDO> list = dstBlockModService.getDstBlockModPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "模组黑名单,此名单上的模组,将会警告提示/禁止添加.xls", "数据", DstBlockModRespVO.class,
                BeanUtils.toBean(list, DstBlockModRespVO.class));
    }

}