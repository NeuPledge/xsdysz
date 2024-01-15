package cn.iocoder.yudao.module.game.service.console.dst;

import cn.iocoder.yudao.module.game.common.params.LoginParam;
import cn.iocoder.yudao.module.game.common.params.PasswordParam;
import cn.iocoder.yudao.module.game.common.params.manage.RentBundleParam;
import cn.iocoder.yudao.module.game.common.params.manage.UserParam;
import cn.iocoder.yudao.module.game.common.response.LoginResponse;
import cn.iocoder.yudao.module.game.common.response.RentInfo;
import cn.iocoder.yudao.module.game.dal.dataobject.operatelog.GameOperateLogDO;
import cn.iocoder.yudao.module.game.dal.dataobject.player.PlayerDO;

import java.util.List;

public interface IUserService {

    /**
     * 登录.
     *
     * @param param user+passwd.
     * @return token.
     */
    LoginResponse login(LoginParam param);

    RentInfo getRent();

    void modifyPassword(PasswordParam param);

    // 管理端

    /**
     * 自动生成账号.
     */
    void createUserAccount();

    void resetPassword(Long userId);

    /**
     * 列出当前的用户.
     */
//    PageInfo listUsers(UserListParam param);

    /**
     * 编辑/更新账号租约信息.
     */
    void updateUserDetail(UserParam userParam);

    /**
     * 列出现有用户(用于绑定租约的用户快速选项).
     *
     * @return 简短的用户信息.
     */
//    List<MBTUserEntity> listUsersForAutodcomplete();

    /**
     * 新增账号绑定租约.
     */
    void createRentBundle(RentBundleParam rentBundleParam);

    /**
     * 编辑/更新账号租约信息.
     */
    void updateRentDetail(RentBundleParam rentBundleParam);

    /**
     * 一般是初始化账号, 给用户分配账号的时候会用这个接口. 一键更新租约起始时间为当前时间, 结束时间为下个月的今天. 默认所有情况都是只租一个月.
     */
    void updateRentDate(Long rentId);

    /**
     * 更新租约时间, 一般是赠送、补偿、续费增加时长.
     *
     * @param field 时间单位. 取值 day, month
     */
    void updateRentalTime(Long rentId, String field, int amount);

    /**
     * 强行结束合约, 一般是用户要求终止租赁时候, 结束合约.
     *
     * @param rentId 租约ID.
     */
    void stopRent(Long rentId);

    /**
     * 租约列表.
     *
     * @param param 查询参数.
     */
//    PageInfo listUserRents(UserRentListParam param);

    void deleteUserAccount(Long userId);

    /**
     * 删除租约.
     *
     * @param rentId 租约ID.
     */
    void deleteRent(Long rentId);

    /**
     * 售出.
     *
     * @param rentId 租约ID.
     */
    void sale(Long rentId);

    List<GameOperateLogDO> getUserOperateLog(Long userId);

    List<PlayerDO> getUserPlayGameLog(Long userId);

    void sendSmsRemindUser();


    /**
     * 一键启动测试房间.
     *
     * @return 房间名称和密码.
     */
    String oneKeyStartTestRoom(Long userId);
}
