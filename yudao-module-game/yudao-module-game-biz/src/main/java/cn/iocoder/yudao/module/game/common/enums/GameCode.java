package cn.iocoder.yudao.module.game.common.enums;


import lombok.Getter;

/**
 * 不同游戏对应的code.
 */
@Getter
public enum GameCode {
    DST("dst", "饥荒联机版", "Don't Starve Together"),
    ;

    private final String code;
    private final String cnName;
    private final String enName;

    GameCode(String code, String cnName, String enName) {
        this.code = code;
        this.cnName = cnName;
        this.enName = enName;
    }

}
