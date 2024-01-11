package cn.iocoder.yudao.module.game.common.enums;

// 操作agent的状态
public enum AgentStatus {
    addpublickey,  // 添加公钥到agent机器上
    install,       // 安装agent
    swap_on,       // 开启swap
    stop_firewalld,// 关闭防火墙
    fetch_cpu_hz,  // 获取cpu hz
    clearInstance, // 清理机器/还原机器
    update,        // 更新agent
    query_dst_agent_version,        // 查询dstagent版本
    query_dst_version,        // 查询饥荒版本
    start,         // 启动agent
    forceStartAgent,         // 强制启动agent
    stop,          // 停止agent
    wipe_screen,   // 清理残余screen
    set_default_token, // 设置默认token
    markSale, // 标记售出
}
