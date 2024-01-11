package cn.iocoder.yudao.module.game.common.enums.dst;

public enum GameMode {
//survival生存/endless无尽/wilderness荒野/lavaarena熔炉/quagmire暴食

    survival("survival", "生存"),
    endless("endless", "无尽"),
    wilderness("wilderness", "荒野"),
    relaxed("relaxed", "轻松"),
    lightsout("lightsout", "暗无天日"),
    lavaarena("lavaarena", "熔炉"),
    quagmire("quagmire", "暴食"),

    // 熊猫云服自定义模式(在轻松的基础上添加的)
    recommend_performance("recommend_performance", "推荐性能优化模式"),

    ;

    private String code;
    private String name;

    GameMode(String code, String name) {
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
