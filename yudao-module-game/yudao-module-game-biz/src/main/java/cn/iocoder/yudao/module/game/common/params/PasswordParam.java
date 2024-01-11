package cn.iocoder.yudao.module.game.common.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PasswordParam {

    @NotBlank(message = "旧密码不可以为空")
    @Schema(description = "旧密码")
    String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 20)
    @Schema(description = "新密码")
    String newPassword;

}
