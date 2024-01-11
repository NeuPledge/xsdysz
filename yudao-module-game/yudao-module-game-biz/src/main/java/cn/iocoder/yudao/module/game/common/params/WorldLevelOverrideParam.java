package cn.iocoder.yudao.module.game.common.params;

import cn.iocoder.yudao.module.game.common.enums.dst.WorldType;
import lombok.Data;

@Data
public class WorldLevelOverrideParam {

    private WorldType worldType;

    private WorldOverride worldOverride;


    @Data
    public static class WorldOverride {

        String raw; // 原始内容
        String override;// 从raw中提取出来的overrides:{}部分，用于前后端assemble struct
    }

}
