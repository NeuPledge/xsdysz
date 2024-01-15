package cn.iocoder.yudao.module.game.controller.console.dst;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "用户信息/租约/登录相关接口")
@RequestMapping("/console/user")
public class ConsoleUserController {

//    @Autowired
//    IUserService userService;
//
//    @Autowired
//    ICommonService commonService;
//
//    @Autowired
//    MBTUserDao userDao;

//    @NoAuth
//    @Operation(summary = "登录")
//    @PostMapping("/login")
//    public ResultData<LoginResponse> login(@RequestBody LoginParam param) {
//        if (StringUtils.isNotEmpty(param.getUsername())) {
//            param.setUsername(param.getUsername().trim());
//        }
//        if (StringUtils.isNotEmpty(param.getPassword())) {
//            param.setPassword(param.getPassword().trim());
//        }
//        LoginResponse response = userService.login(param);
//        commonService.saveOperateLog(response.getUserId(), "登录");
//        return ResultData.withDataSuccess(response);
//    }

//
//    @EitherOneAuth
//    @Operation(summary = "修改密码")
//    @PostMapping("/modify-password")
//    public ResultData modifyPassword(@RequestBody PasswordParam param) {
//        userService.modifyPassword(param);
//        commonService.saveOperateLog("修改密码");
//        return ResultData.success();
//    }

//
//    @Operation(summary = "获取用户租约信息")
//    @GetMapping("/rent-info")
//    public ResultData<RentInfo> getRentInfo() {
//        RentInfo rentInfo = userService.getRent();
//        return ResultData.withDataSuccess(rentInfo);
//    }
//
//    @Operation(summary = "授权更新版本后自动重启存档")
//    @GetMapping("/auto_restart_authorization")
//    public ResultData<RentInfo> autoRestartAuthorization(@RequestParam Boolean value) {
//        Long userId = HeaderHolder.getCurrentUserId();
//        MBTUserEntity record = new MBTUserEntity();
//        record.setId(userId);
//        value = value == null ? false : value;
//        record.setAutoRestartAuthorization(value);
//        userDao.updateByPrimaryKeySelective(record, Column.autoRestartAuthorization);
//        return ResultData.success();
//    }

}
