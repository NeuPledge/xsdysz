package cn.iocoder.yudao.module.game.controller.console.dst;


import com.dooyo.annotations.EitherOneAuth;
import com.dooyo.annotations.NoAuth;
import cn.iocoder.yudao.module.game.common.ResultData;
import cn.iocoder.yudao.module.game.common.params.LoginParam;
import cn.iocoder.yudao.module.game.common.params.PasswordParam;
import cn.iocoder.yudao.module.game.common.response.LoginResponse;
import cn.iocoder.yudao.module.game.common.response.RentInfo;
import com.dooyo.dao.MBTUserDao;
import com.dooyo.dao.entity.MBTUserEntity;
import com.dooyo.dao.entity.MBTUserEntity.Column;
import cn.iocoder.yudao.module.game.framework.HeaderHolder;
import com.dooyo.service.ICommonService;
import com.dooyo.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "用户信息/租约/登录相关接口")
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    ICommonService commonService;

    @Autowired
    MBTUserDao userDao;

    @NoAuth
    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResultData<LoginResponse> login(@RequestBody LoginParam param) {
        if (StringUtils.isNotEmpty(param.getUsername())) {
            param.setUsername(param.getUsername().trim());
        }
        if (StringUtils.isNotEmpty(param.getPassword())) {
            param.setPassword(param.getPassword().trim());
        }
        LoginResponse response = userService.login(param);
        commonService.saveOperateLog(response.getUserId(), "登录");
        return ResultData.withDataSuccess(response);
    }


    @EitherOneAuth
    @Operation(summary = "修改密码")
    @PostMapping("/modify-password")
    public ResultData modifyPassword(@RequestBody PasswordParam param) {
        userService.modifyPassword(param);
        commonService.saveOperateLog("修改密码");
        return ResultData.success();
    }


    @Operation(summary = "获取用户租约信息")
    @GetMapping("/rent-info")
    public ResultData<RentInfo> getRentInfo() {
        RentInfo rentInfo = userService.getRent();
        return ResultData.withDataSuccess(rentInfo);
    }

    @Operation(summary = "授权更新版本后自动重启存档")
    @GetMapping("/auto_restart_authorization")
    public ResultData<RentInfo> autoRestartAuthorization(@RequestParam Boolean value) {
        Long userId = HeaderHolder.getCurrentUserId();
        MBTUserEntity record = new MBTUserEntity();
        record.setId(userId);
        value = value == null ? false : value;
        record.setAutoRestartAuthorization(value);
        userDao.updateByPrimaryKeySelective(record, Column.autoRestartAuthorization);
        return ResultData.success();
    }

    @Operation(summary = "已经展示过好评弹窗")
    @GetMapping("/has-show-hao-ping-dialog")
    public ResultData hasShowHaoPingDialog() {
        Long userId = HeaderHolder.getCurrentUserId();
        MBTUserEntity record = new MBTUserEntity();
        record.setId(userId);
        record.setHaoPingDialog(true);
        userDao.updateByPrimaryKeySelective(record, Column.haoPingDialog);
        return ResultData.success();
    }


}
