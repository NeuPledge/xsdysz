package cn.iocoder.yudao.module.game.framework;

import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;

public class HeaderHolder {

    private static final ThreadLocal<AdminUserRespDTO> CURRENT_USER = new ThreadLocal<>();
    private static final ThreadLocal<RentDO> CURRENT_USER_RENT = new ThreadLocal<>();



    public static void setCurrentUser(AdminUserRespDTO user) {
        CURRENT_USER.set(user);
    }

    public static AdminUserRespDTO getCurrentUser() {
        return CURRENT_USER.get();
    }


    public static void setCurrentUserRent(RentDO userRent) {
        CURRENT_USER_RENT.set(userRent);
    }

    public static RentDO getCurrentUserRent() {
        return CURRENT_USER_RENT.get();
    }

    public static Long getCurrentUserId() {
        if (CURRENT_USER.get() == null) {
            return null;
        }
        return CURRENT_USER.get().getId();
    }

    public static void remove() {
        CURRENT_USER.remove();
        CURRENT_USER_RENT.remove();
    }
}
