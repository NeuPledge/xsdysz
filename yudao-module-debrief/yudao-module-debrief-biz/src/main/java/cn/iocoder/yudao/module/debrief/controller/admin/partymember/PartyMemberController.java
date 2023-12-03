package cn.iocoder.yudao.module.debrief.controller.admin.partymember;

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

import cn.iocoder.yudao.module.debrief.controller.admin.partymember.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.partymember.PartyMemberDO;
import cn.iocoder.yudao.module.debrief.service.partymember.PartyMemberService;

@Tag(name = "管理后台 - 党员信息")
@RestController
@RequestMapping("/debrief/party-member")
@Validated
public class PartyMemberController {

    @Resource
    private PartyMemberService partyMemberService;

    @PostMapping("/create")
    @Operation(summary = "创建党员信息")
    @PreAuthorize("@ss.hasPermission('debrief:party-member:create')")
    public CommonResult<Long> createPartyMember(@Valid @RequestBody PartyMemberSaveReqVO createReqVO) {
        return success(partyMemberService.createPartyMember(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新党员信息")
    @PreAuthorize("@ss.hasPermission('debrief:party-member:update')")
    public CommonResult<Boolean> updatePartyMember(@Valid @RequestBody PartyMemberSaveReqVO updateReqVO) {
        partyMemberService.updatePartyMember(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除党员信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('debrief:party-member:delete')")
    public CommonResult<Boolean> deletePartyMember(@RequestParam("id") Long id) {
        partyMemberService.deletePartyMember(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得党员信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('debrief:party-member:query')")
    public CommonResult<PartyMemberRespVO> getPartyMember(@RequestParam("id") Long id) {
        PartyMemberDO partyMember = partyMemberService.getPartyMember(id);
        return success(BeanUtils.toBean(partyMember, PartyMemberRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得党员信息分页")
    @PreAuthorize("@ss.hasPermission('debrief:party-member:query')")
    public CommonResult<PageResult<PartyMemberRespVO>> getPartyMemberPage(@Valid PartyMemberPageReqVO pageReqVO) {
        PageResult<PartyMemberDO> pageResult = partyMemberService.getPartyMemberPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PartyMemberRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出党员信息 Excel")
    @PreAuthorize("@ss.hasPermission('debrief:party-member:export')")
    @OperateLog(type = EXPORT)
    public void exportPartyMemberExcel(@Valid PartyMemberPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PartyMemberDO> list = partyMemberService.getPartyMemberPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "党员信息.xls", "数据", PartyMemberRespVO.class,
                        BeanUtils.toBean(list, PartyMemberRespVO.class));
    }

}