package cn.iocoder.yudao.module.game.common.consts;

import cn.iocoder.yudao.module.game.common.enums.MsgBox;
import lombok.Data;

/**
 * 业务异常. 非系统问题, 而是用户非法操作造成.
 */
@Data
public class BusinessException extends RuntimeException {

    private int code;
    private String message;

    public BusinessException(String message) {
        this.code = MsgBox.FAILED.getCode();
        this.message = message;
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
