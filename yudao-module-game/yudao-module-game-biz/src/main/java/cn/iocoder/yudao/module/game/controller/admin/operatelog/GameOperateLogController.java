package cn.iocoder.yudao.module.game.controller.admin.operatelog;

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

import cn.iocoder.yudao.module.game.controller.admin.operatelog.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.operatelog.GameOperateLogDO;
import cn.iocoder.yudao.module.game.service.operatelog.GameOperateLogService;

@Tag(name = "管理后台 - 操作日志")
@RestController
@RequestMapping("/game/operate-log")
@Validated
public class GameOperateLogController {

    @Resource
    private GameOperateLogService gameOperateLogService;

    @PostMapping("/create")
    @Operation(summary = "创建操作日志")
    @PreAuthorize("@ss.hasPermission('game:operate-log:create')")
    public CommonResult<Long> createOperateLog(@Valid @RequestBody OperateLogSaveReqVO createReqVO) {
        return success(gameOperateLogService.createOperateLog(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新操作日志")
    @PreAuthorize("@ss.hasPermission('game:operate-log:update')")
    public CommonResult<Boolean> updateOperateLog(@Valid @RequestBody OperateLogSaveReqVO updateReqVO) {
        gameOperateLogService.updateOperateLog(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除操作日志")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('game:operate-log:delete')")
    public CommonResult<Boolean> deleteOperateLog(@RequestParam("id") Long id) {
        gameOperateLogService.deleteOperateLog(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得操作日志")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('game:operate-log:query')")
    public CommonResult<OperateLogRespVO> getOperateLog(@RequestParam("id") Long id) {
        GameOperateLogDO operateLog = gameOperateLogService.getOperateLog(id);
        return success(BeanUtils.toBean(operateLog, OperateLogRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得操作日志分页")
    @PreAuthorize("@ss.hasPermission('game:operate-log:query')")
    public CommonResult<PageResult<OperateLogRespVO>> getOperateLogPage(@Valid OperateLogPageReqVO pageReqVO) {
        PageResult<GameOperateLogDO> pageResult = gameOperateLogService.getOperateLogPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OperateLogRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出操作日志 Excel")
    @PreAuthorize("@ss.hasPermission('game:operate-log:export')")
    @OperateLog(type = EXPORT)
    public void exportOperateLogExcel(@Valid OperateLogPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<GameOperateLogDO> list = gameOperateLogService.getOperateLogPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "操作日志.xls", "数据", OperateLogRespVO.class,
                        BeanUtils.toBean(list, OperateLogRespVO.class));
    }

}