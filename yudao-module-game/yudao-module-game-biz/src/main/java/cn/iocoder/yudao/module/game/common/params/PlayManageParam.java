package cn.iocoder.yudao.module.game.common.params;

import lombok.Data;

@Data
public class PlayManageParam {

    private String kleiId;
    private ManageType type;

    public enum ManageType {
        killToDeath,      // 杀死玩家
        respawnfromghost, // 复活玩家
        kick,             // 踢出游戏
        despawn,          // 重选角色
    }
}
