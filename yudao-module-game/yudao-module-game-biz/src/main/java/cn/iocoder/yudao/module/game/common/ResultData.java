package cn.iocoder.yudao.module.game.common;


import cn.iocoder.yudao.module.game.common.enums.MsgBox;
import lombok.Data;

@Data
public class ResultData<T> {

    private int code;
    private String message;

    /**
     * 加密后的数据
     **/
    private Object data;
    private Boolean encrypt = true;

    public ResultData() {
        this.code = MsgBox.SUCCESS.getCode();
        this.message = MsgBox.SUCCESS.getMsg();
    }

    public ResultData(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultData(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResultData(int code, String message, Object data, Boolean encrypt) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.encrypt = encrypt;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
//        this.data = AESUtil.CBCEncrypt(JSON.toJSONString(data));
    }

    public static <T> ResultData<T> success() {
        return new ResultData<>(MsgBox.SUCCESS.getCode(), MsgBox.SUCCESS.getMsg());
    }

    public static <T> ResultData<T> success(String msg) {
        return new ResultData<>(MsgBox.SUCCESS.getCode(), msg);
    }

    public static <T> ResultData<T> withDataSuccessNoEncrypt(T o) {
        return new ResultData<>(MsgBox.SUCCESS.getCode(), MsgBox.SUCCESS.getMsg(), o, false);
    }

    public static <T> ResultData<T> withDataSuccess(T o) {
        return new ResultData<>(MsgBox.SUCCESS.getCode(), MsgBox.SUCCESS.getMsg(), o);
    }

    public static <T> ResultData<T> withDataSuccess(T o, String message) {
        return new ResultData<>(MsgBox.SUCCESS.getCode(), message, o);
    }

    public static <T> ResultData<T> error() {
        return new ResultData<>(MsgBox.FAILED.getCode(), MsgBox.FAILED.getMsg());
    }

    public static <T> ResultData<T> error(String msg) {
        return new ResultData<>(MsgBox.FAILED.getCode(), msg);
    }

    public static <T> ResultData<T> needLogin() {
        return new ResultData<>(MsgBox.AUTH_FAILED.getCode(), MsgBox.AUTH_FAILED.getMsg());
    }

}

