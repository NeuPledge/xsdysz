package cn.iocoder.yudao.module.game.common.enums.dst;

public enum DayPhase {

    day("白天"),
    dusk("黄昏"),
    night("夜晚"),
    ;

    private final String name;

    DayPhase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
