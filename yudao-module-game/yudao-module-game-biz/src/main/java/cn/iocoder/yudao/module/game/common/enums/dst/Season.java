package cn.iocoder.yudao.module.game.common.enums.dst;

public enum Season {
    spring("春季"),
    summer("夏季"),
    autumn("秋季"),
    winter("冬季"),
    ;

    private final String name;

    Season(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
