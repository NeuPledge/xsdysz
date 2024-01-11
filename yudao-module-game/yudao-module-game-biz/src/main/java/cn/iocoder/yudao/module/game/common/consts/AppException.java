package cn.iocoder.yudao.module.game.common.consts;

import cn.iocoder.yudao.module.game.common.enums.MsgBox;
import lombok.Data;

@Data
public class AppException extends RuntimeException{

    private int code;
    private String message;

    public AppException(String message){
        this.code = MsgBox.FAILED.getCode();
        this.message = message+" 你可以先刷新页面尝试几次，如仍报错请在客服群联系我们为您处理~";
    }
}
