package cn.iocoder.yudao.module.game.common.enums;


/**
 * 0 - 100 预留
 * 101 - 999 为通用异常
 * 200 成功 400 失败 500 异常 401 权限验证失败
 * 自定义异常从1000开始，每个业务段为1000个，A业务1000 - 1999 B业务2000 - 2999
 */
public enum MsgBox {

    FAILED(0, "请求失败", "请求失败描述"),
    SUCCESS(200, "请求成功", "请求成功描述"),
    EXISTS(2, "该用户名已经注册", "已经注册！"),
    LOGIN_FAILED(3, "用户名或密码错误！", "用户名或密码错误"),
    NETWOKR_ERROR(500, "系统网络异常", "系统网络异常"),
    AUTH_FAILED(401, "请您登录！","权限验证异常"),
    NEED_UPGRADE(10, "请您升级新版本", "升级提示");

    private int code;
    private String msg;
    private String desc;

    MsgBox(int code, String msg, String desc){
        this.code = code;
        this.msg = msg;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
