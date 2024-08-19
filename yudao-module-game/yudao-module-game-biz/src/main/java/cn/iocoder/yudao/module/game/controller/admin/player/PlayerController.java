package cn.iocoder.yudao.module.game.controller.admin.player;

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




import cn.iocoder.yudao.module.game.controller.admin.player.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.player.PlayerDO;
import cn.iocoder.yudao.module.game.service.player.PlayerService;

@Tag(name = "管理后台 - 记录局内玩家人数")
@RestController
@RequestMapping("/game/player")
@Validated
public class PlayerController {

    @Resource
    private PlayerService playerService;

    @PostMapping("/create")
    @Operation(summary = "创建记录局内玩家人数")
    @PreAuthorize("@ss.hasPermission('game:player:create')")
    public CommonResult<Long> createPlayer(@Valid @RequestBody PlayerSaveReqVO createReqVO) {
        return success(playerService.createPlayer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新记录局内玩家人数")
    @PreAuthorize("@ss.hasPermission('game:player:update')")
    public CommonResult<Boolean> updatePlayer(@Valid @RequestBody PlayerSaveReqVO updateReqVO) {
        playerService.updatePlayer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除记录局内玩家人数")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('game:player:delete')")
    public CommonResult<Boolean> deletePlayer(@RequestParam("id") Long id) {
        playerService.deletePlayer(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得记录局内玩家人数")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('game:player:query')")
    public CommonResult<PlayerRespVO> getPlayer(@RequestParam("id") Long id) {
        PlayerDO player = playerService.getPlayer(id);
        return success(BeanUtils.toBean(player, PlayerRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得记录局内玩家人数分页")
    @PreAuthorize("@ss.hasPermission('game:player:query')")
    public CommonResult<PageResult<PlayerRespVO>> getPlayerPage(@Valid PlayerPageReqVO pageReqVO) {
        PageResult<PlayerDO> pageResult = playerService.getPlayerPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PlayerRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出记录局内玩家人数 Excel")
    @PreAuthorize("@ss.hasPermission('game:player:export')")
    
    public void exportPlayerExcel(@Valid PlayerPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PlayerDO> list = playerService.getPlayerPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "记录局内玩家人数.xls", "数据", PlayerRespVO.class,
                        BeanUtils.toBean(list, PlayerRespVO.class));
    }

}