package cn.iocoder.yudao.module.game.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginResponse {

    private String token;
    private Long userId;
    private String role;
    @Schema(description = "租约剩余天数")
    private long leftDays;
}
