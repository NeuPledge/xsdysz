package cn.iocoder.yudao.module.game.common.params.manage;

import lombok.Data;

@Data
public class UserParam {
    private Long userId;
    private String username;
    private String phone;
    private String notes;
}
