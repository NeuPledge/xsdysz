package cn.iocoder.yudao.module.game.common.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginParam {

    @NotBlank(message = "账号不能为空")
    @Schema(description = "账号")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

}
