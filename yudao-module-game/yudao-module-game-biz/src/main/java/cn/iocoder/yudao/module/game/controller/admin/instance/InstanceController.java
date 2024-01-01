package cn.iocoder.yudao.module.game.controller.admin.instance;

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

import cn.iocoder.yudao.module.game.controller.admin.instance.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.service.instance.InstanceService;

@Tag(name = "管理后台 - 机器信息")
@RestController
@RequestMapping("/game/instance")
@Validated
public class InstanceController {

    @Resource
    private InstanceService instanceService;

    @PostMapping("/create")
    @Operation(summary = "创建机器信息")
    @PreAuthorize("@ss.hasPermission('game:instance:create')")
    public CommonResult<Long> createInstance(@Valid @RequestBody InstanceSaveReqVO createReqVO) {
        return success(instanceService.createInstance(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新机器信息")
    @PreAuthorize("@ss.hasPermission('game:instance:update')")
    public CommonResult<Boolean> updateInstance(@Valid @RequestBody InstanceSaveReqVO updateReqVO) {
        instanceService.updateInstance(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除机器信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('game:instance:delete')")
    public CommonResult<Boolean> deleteInstance(@RequestParam("id") Long id) {
        instanceService.deleteInstance(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得机器信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('game:instance:query')")
    public CommonResult<InstanceRespVO> getInstance(@RequestParam("id") Long id) {
        InstanceDO instance = instanceService.getInstance(id);
        return success(BeanUtils.toBean(instance, InstanceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得机器信息分页")
    @PreAuthorize("@ss.hasPermission('game:instance:query')")
    public CommonResult<PageResult<InstanceRespVO>> getInstancePage(@Valid InstancePageReqVO pageReqVO) {
        PageResult<InstanceDO> pageResult = instanceService.getInstancePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InstanceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出机器信息 Excel")
    @PreAuthorize("@ss.hasPermission('game:instance:export')")
    @OperateLog(type = EXPORT)
    public void exportInstanceExcel(@Valid InstancePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InstanceDO> list = instanceService.getInstancePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "机器信息.xls", "数据", InstanceRespVO.class,
                        BeanUtils.toBean(list, InstanceRespVO.class));
    }

}