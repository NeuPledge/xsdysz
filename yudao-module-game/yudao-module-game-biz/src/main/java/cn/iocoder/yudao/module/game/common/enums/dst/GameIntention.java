package cn.iocoder.yudao.module.game.common.enums.dst;

public enum GameIntention {

    cooperative("cooperative", "合作"),
    competitive("competitive", "竞争"),
    social("social", "社交"),
    madness("madness", "疯狂"),

    ;

    private String code;
    private String name;

    GameIntention(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
