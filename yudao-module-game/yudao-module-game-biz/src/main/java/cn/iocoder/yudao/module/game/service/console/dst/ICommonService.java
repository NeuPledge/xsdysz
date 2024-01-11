package cn.iocoder.yudao.module.game.service.console.dst;

import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;

public interface ICommonService {


    // 保存操作日志.
    void saveOperateLog(String name);
    void saveOperateLog(Long uid, String name);


    // 设置对等端口
    void setMasterEqualPort(InstanceDO instance);

    // 设置对等端口
    void setCavesEqualPort(InstanceDO instance);

    String forceStartAgent(InstanceDO instance);
}
