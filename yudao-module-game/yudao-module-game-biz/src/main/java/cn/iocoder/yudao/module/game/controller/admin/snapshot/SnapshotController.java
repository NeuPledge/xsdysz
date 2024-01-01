package cn.iocoder.yudao.module.game.controller.admin.snapshot;

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

import cn.iocoder.yudao.module.game.controller.admin.snapshot.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.snapshot.SnapshotDO;
import cn.iocoder.yudao.module.game.service.snapshot.SnapshotService;

@Tag(name = "管理后台 - 快照, 便于误操作恢复数据")
@RestController
@RequestMapping("/game/snapshot")
@Validated
public class SnapshotController {

    @Resource
    private SnapshotService snapshotService;

    @PostMapping("/create")
    @Operation(summary = "创建快照, 便于误操作恢复数据")
    @PreAuthorize("@ss.hasPermission('game:snapshot:create')")
    public CommonResult<Long> createSnapshot(@Valid @RequestBody SnapshotSaveReqVO createReqVO) {
        return success(snapshotService.createSnapshot(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新快照, 便于误操作恢复数据")
    @PreAuthorize("@ss.hasPermission('game:snapshot:update')")
    public CommonResult<Boolean> updateSnapshot(@Valid @RequestBody SnapshotSaveReqVO updateReqVO) {
        snapshotService.updateSnapshot(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除快照, 便于误操作恢复数据")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('game:snapshot:delete')")
    public CommonResult<Boolean> deleteSnapshot(@RequestParam("id") Long id) {
        snapshotService.deleteSnapshot(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得快照, 便于误操作恢复数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('game:snapshot:query')")
    public CommonResult<SnapshotRespVO> getSnapshot(@RequestParam("id") Long id) {
        SnapshotDO snapshot = snapshotService.getSnapshot(id);
        return success(BeanUtils.toBean(snapshot, SnapshotRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得快照, 便于误操作恢复数据分页")
    @PreAuthorize("@ss.hasPermission('game:snapshot:query')")
    public CommonResult<PageResult<SnapshotRespVO>> getSnapshotPage(@Valid SnapshotPageReqVO pageReqVO) {
        PageResult<SnapshotDO> pageResult = snapshotService.getSnapshotPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SnapshotRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出快照, 便于误操作恢复数据 Excel")
    @PreAuthorize("@ss.hasPermission('game:snapshot:export')")
    @OperateLog(type = EXPORT)
    public void exportSnapshotExcel(@Valid SnapshotPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SnapshotDO> list = snapshotService.getSnapshotPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "快照, 便于误操作恢复数据.xls", "数据", SnapshotRespVO.class,
                        BeanUtils.toBean(list, SnapshotRespVO.class));
    }

}