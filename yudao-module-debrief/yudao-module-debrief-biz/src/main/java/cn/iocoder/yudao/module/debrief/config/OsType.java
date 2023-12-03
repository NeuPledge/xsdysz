package cn.iocoder.yudao.module.debrief.config;

public enum OsType {
    LINUX, WINDOWS;


    public static OsType getType(String osInfo) {
        for (OsType type : values()) {
            if (osInfo.toUpperCase().contains(type.name())) {
                return type;
            }
        }
        return LINUX;
    }
}
