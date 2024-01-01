package cn.iocoder.yudao.module.game.controller.admin.gamepackage;

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

import cn.iocoder.yudao.module.game.controller.admin.gamepackage.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.gamepackage.PackageDO;
import cn.iocoder.yudao.module.game.service.gamepackage.PackageService;

@Tag(name = "管理后台 - 饥荒套餐配置")
@RestController
@RequestMapping("/game/package")
@Validated
public class PackageController {

    @Resource
    private PackageService packageService;

    @PostMapping("/create")
    @Operation(summary = "创建饥荒套餐配置")
    @PreAuthorize("@ss.hasPermission('game:package:create')")
    public CommonResult<Long> createPackage(@Valid @RequestBody PackageSaveReqVO createReqVO) {
        return success(packageService.createPackage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新饥荒套餐配置")
    @PreAuthorize("@ss.hasPermission('game:package:update')")
    public CommonResult<Boolean> updatePackage(@Valid @RequestBody PackageSaveReqVO updateReqVO) {
        packageService.updatePackage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除饥荒套餐配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('game:package:delete')")
    public CommonResult<Boolean> deletePackage(@RequestParam("id") Long id) {
        packageService.deletePackage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得饥荒套餐配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('game:package:query')")
    public CommonResult<PackageRespVO> getPackage(@RequestParam("id") Long id) {
        PackageDO gamePackage = packageService.getPackage(id);
        return success(BeanUtils.toBean(gamePackage, PackageRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得饥荒套餐配置分页")
    @PreAuthorize("@ss.hasPermission('game:package:query')")
    public CommonResult<PageResult<PackageRespVO>> getPackagePage(@Valid PackagePageReqVO pageReqVO) {
        PageResult<PackageDO> pageResult = packageService.getPackagePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PackageRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出饥荒套餐配置 Excel")
    @PreAuthorize("@ss.hasPermission('game:package:export')")
    @OperateLog(type = EXPORT)
    public void exportPackageExcel(@Valid PackagePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PackageDO> list = packageService.getPackagePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "饥荒套餐配置.xls", "数据", PackageRespVO.class,
                        BeanUtils.toBean(list, PackageRespVO.class));
    }

}