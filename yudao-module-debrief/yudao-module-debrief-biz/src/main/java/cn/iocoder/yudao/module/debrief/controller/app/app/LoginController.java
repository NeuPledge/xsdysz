package cn.iocoder.yudao.module.debrief.controller.app.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.debrief.controller.app.request.LoginRequest;
import cn.iocoder.yudao.module.debrief.service.LoginService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Tag(name = "Debrief-APP - 列表")
@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping
    public CommonResult<Long> login(@RequestBody LoginRequest loginRequest) {
        return success(loginService.login(loginRequest));
    }
}
