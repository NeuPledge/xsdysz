package cn.iocoder.yudao.module.game.util;


import cn.iocoder.yudao.module.game.common.consts.AppException;

public class Assert {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new AppException(message);
        }
    }

}
