package cn.iocoder.yudao.module.game.enums;// TODO 待办：请将下面的错误码复制到 yudao-module-game-api 模块的 ErrorCodeConstants 类中。注意，请给“TODO 补充编号”设置一个错误码编号！！！

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    // GAME
    ErrorCode RENT_NOT_EXISTS = new ErrorCode(2100_001_001, "租约不存在");
    ErrorCode INSTANCE_NOT_EXISTS = new ErrorCode(2100_001_002, "机器信息不存在");
    ErrorCode OPERATE_LOG_NOT_EXISTS = new ErrorCode(2100_001_003, "操作日志不存在");
    ErrorCode IDC_HOST_NOT_EXISTS = new ErrorCode(2100_001_004, "机房主机不存在");
    ErrorCode PLAYER_NOT_EXISTS = new ErrorCode(2100_001_005, "记录局内玩家人数不存在");
    ErrorCode SNAPSHOT_NOT_EXISTS = new ErrorCode(2100_001_006, "快照, 便于误操作恢复数据不存在");
    ErrorCode PACKAGE_NOT_EXISTS = new ErrorCode(2100_001_007, "套餐配置不存在");

    // DST
    ErrorCode DST_WORLD_OPTION_NOT_EXISTS = new ErrorCode(2100_002_001, "饥荒世界配置初始化数据不存在");
    ErrorCode DST_BLOCK_MOD_NOT_EXISTS = new ErrorCode(2100_002_002, "模组黑名单,此名单上的模组,将会警告提示/禁止添加不存在");
    ErrorCode DST_MODS_NOT_EXISTS = new ErrorCode(2100_003_003, "饥荒模组信息不存在");


}
