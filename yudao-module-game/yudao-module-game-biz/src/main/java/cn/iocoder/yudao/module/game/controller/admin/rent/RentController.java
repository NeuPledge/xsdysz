package cn.iocoder.yudao.module.game.controller.admin.rent;

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




import cn.iocoder.yudao.module.game.controller.admin.rent.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;
import cn.iocoder.yudao.module.game.service.rent.RentService;

@Tag(name = "管理后台 - 租约")
@RestController
@RequestMapping("/game/rent")
@Validated
public class RentController {

    @Resource
    private RentService rentService;

    @PostMapping("/create")
    @Operation(summary = "创建租约")
    @PreAuthorize("@ss.hasPermission('game:rent:create')")
    public CommonResult<Long> createRent(@Valid @RequestBody RentSaveReqVO createReqVO) {
        return success(rentService.createRent(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新租约")
    @PreAuthorize("@ss.hasPermission('game:rent:update')")
    public CommonResult<Boolean> updateRent(@Valid @RequestBody RentSaveReqVO updateReqVO) {
        rentService.updateRent(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除租约")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('game:rent:delete')")
    public CommonResult<Boolean> deleteRent(@RequestParam("id") Long id) {
        rentService.deleteRent(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得租约")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('game:rent:query')")
    public CommonResult<RentRespVO> getRent(@RequestParam("id") Long id) {
        RentDO rent = rentService.getRent(id);
        return success(BeanUtils.toBean(rent, RentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得租约分页")
    @PreAuthorize("@ss.hasPermission('game:rent:query')")
    public CommonResult<PageResult<RentRespVO>> getRentPage(@Valid RentPageReqVO pageReqVO) {
        PageResult<RentDO> pageResult = rentService.getRentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RentRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出租约 Excel")
    @PreAuthorize("@ss.hasPermission('game:rent:export')")
    
    public void exportRentExcel(@Valid RentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<RentDO> list = rentService.getRentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "租约.xls", "数据", RentRespVO.class,
                        BeanUtils.toBean(list, RentRespVO.class));
    }

}