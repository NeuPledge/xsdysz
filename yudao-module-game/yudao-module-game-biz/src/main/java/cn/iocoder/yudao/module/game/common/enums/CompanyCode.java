package cn.iocoder.yudao.module.game.common.enums;


import lombok.Getter;

/**
 * 服务器云厂商的code;
 */
@Deprecated
@Getter
public enum CompanyCode {
    TENCENT("tencent", "腾讯云"),
    VMTest("vmware", "本地测试虚拟"),
    TIANYIYUN("tianyiyun", "天翼云"),
    TENCENT_T("tencent_t", "腾讯新加坡(T云代理)"),
    ;

    private final String code;
    private final String name;

    CompanyCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
