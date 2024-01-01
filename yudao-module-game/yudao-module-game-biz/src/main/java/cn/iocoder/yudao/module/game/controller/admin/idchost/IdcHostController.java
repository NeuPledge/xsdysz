package cn.iocoder.yudao.module.game.controller.admin.idchost;

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

import cn.iocoder.yudao.module.game.controller.admin.idchost.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.idchost.IdcHostDO;
import cn.iocoder.yudao.module.game.service.idchost.IdcHostService;

@Tag(name = "管理后台 - 机房主机")
@RestController
@RequestMapping("/game/idc-host")
@Validated
public class IdcHostController {

    @Resource
    private IdcHostService idcHostService;

    @PostMapping("/create")
    @Operation(summary = "创建机房主机")
    @PreAuthorize("@ss.hasPermission('game:idc-host:create')")
    public CommonResult<Long> createIdcHost(@Valid @RequestBody IdcHostSaveReqVO createReqVO) {
        return success(idcHostService.createIdcHost(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新机房主机")
    @PreAuthorize("@ss.hasPermission('game:idc-host:update')")
    public CommonResult<Boolean> updateIdcHost(@Valid @RequestBody IdcHostSaveReqVO updateReqVO) {
        idcHostService.updateIdcHost(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除机房主机")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('game:idc-host:delete')")
    public CommonResult<Boolean> deleteIdcHost(@RequestParam("id") Long id) {
        idcHostService.deleteIdcHost(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得机房主机")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('game:idc-host:query')")
    public CommonResult<IdcHostRespVO> getIdcHost(@RequestParam("id") Long id) {
        IdcHostDO idcHost = idcHostService.getIdcHost(id);
        return success(BeanUtils.toBean(idcHost, IdcHostRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得机房主机分页")
    @PreAuthorize("@ss.hasPermission('game:idc-host:query')")
    public CommonResult<PageResult<IdcHostRespVO>> getIdcHostPage(@Valid IdcHostPageReqVO pageReqVO) {
        PageResult<IdcHostDO> pageResult = idcHostService.getIdcHostPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, IdcHostRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出机房主机 Excel")
    @PreAuthorize("@ss.hasPermission('game:idc-host:export')")
    @OperateLog(type = EXPORT)
    public void exportIdcHostExcel(@Valid IdcHostPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<IdcHostDO> list = idcHostService.getIdcHostPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "机房主机.xls", "数据", IdcHostRespVO.class,
                        BeanUtils.toBean(list, IdcHostRespVO.class));
    }

}