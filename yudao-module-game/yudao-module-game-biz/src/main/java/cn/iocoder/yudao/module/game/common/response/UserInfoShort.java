package cn.iocoder.yudao.module.game.common.response;

import lombok.Data;

/**
 * 用于绑定租约快速选择用户id的下拉选项.
 */
@Data
public class UserInfoShort {

    private Long userId;
    private String username;
    private String createTime;

}
