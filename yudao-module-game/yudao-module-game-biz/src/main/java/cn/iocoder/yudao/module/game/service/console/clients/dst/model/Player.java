package cn.iocoder.yudao.module.game.service.console.clients.dst.model;

import lombok.Data;

@Data
public class Player {

    // 序号
    private Integer number;
    // klei 用户序列Id
    private String kleiId;
    // 用户昵称(游戏内昵称, 和steam用户名一致)
    private String nickname;
    // 游戏内使用的角色
    private String character;
}
