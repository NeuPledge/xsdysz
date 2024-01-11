package cn.iocoder.yudao.module.game.common.params.manage;

import cn.iocoder.yudao.module.game.common.params.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListParam extends PageParam {

    private Long userId;

    private String username;
}
