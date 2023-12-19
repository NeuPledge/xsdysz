package cn.iocoder.yudao.module.game.controller.admin.idchost;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 机房主机")
@RestController
@RequestMapping("/game/idc-host")
@Validated
public class IdcHostController {

    @PostMapping("/create")
    @Operation(summary = "创建")
    @PreAuthorize("@ss.hasPermission('debrief:dic-branch:create')")
    public CommonResult<Long> createDicBranch() {
        return success(1L);
    }


}