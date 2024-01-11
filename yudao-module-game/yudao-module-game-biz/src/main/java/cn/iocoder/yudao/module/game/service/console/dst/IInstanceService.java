package cn.iocoder.yudao.module.game.service.console.dst;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.game.common.enums.dst.IniType;
import cn.iocoder.yudao.module.game.common.params.manage.AgentStatusParam;
import cn.iocoder.yudao.module.game.common.params.manage.GameLogParam;
import cn.iocoder.yudao.module.game.common.params.manage.InstanceParam;
import cn.iocoder.yudao.module.game.common.params.manage.InstanceQueryParam;
import cn.iocoder.yudao.module.game.common.response.DstBackupResponse;
import cn.iocoder.yudao.module.game.common.response.HostResourceInfo;
import cn.iocoder.yudao.module.game.common.response.InstanceInfo;
import cn.iocoder.yudao.module.game.common.response.InstanceInfoShort;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;

import java.util.List;

public interface IInstanceService {

    PageResult<InstanceInfo> list(InstanceQueryParam param);

    List<InstanceInfoShort> listInstancesForAutodcomplete(Long packageId);

    /**
     * 新增机器实例.
     */
    void createInstance(InstanceParam param);

    /**
     * 更新机器实例详情.
     */
    void updateInstance(InstanceParam param);

    /**
     * 管理Agent运行状态.
     */
    String operateAgentStatus(AgentStatusParam param);

    Integer nDaysExpiredInstance(Integer days);

    InstanceDO getInstance(Long id);

    /**
     * 删除机器实例.
     */
    void deleteInstance(Long instanceId);

    /**
     * 更新所有机器的Agent到最新版本.
     */
    void updateAllDstAgent();

    // 更新不等于指定版本的Agent到最新版本.
    void updateAllDstAgentToSpecifiedVersion(String version);

    /**
     * 更新所有机器的游戏版本
     */
    void updateAllDstGame();

    HostResourceInfo getResourceInfo(Long instanceId);

    /**
     * 查询机器上饥荒的游戏版本号.
     */
    String getDstGameVersion(InstanceDO instance);

    // 一键查询所有机器上饥荒的游戏版本号
    void getAllInstancesDstGameVersion();

    // 查询install.log
    String checkDstInstallation(Long instanceId);

    String gameLog(GameLogParam gameLogParam);

    List<DstBackupResponse> listBackups(Long instanceId);

    // 跨机器迁移
    void archivesMigration(Long newInstanceId, Long oldInstanceId, String path, Boolean allFiles);

    // 同机器还原
    void archivesMigrationSameMachine(Long instanceId, String path, Boolean allFiles);

    String resetDefaultToken(InstanceDO instance);

    void setEqualPort(Long instanceId);

    String queryIni(Long instanceId, IniType type);

    void wipeScreen(InstanceDO instance);

    void checkClusterExistAndDownload(InstanceDO instance, RentDO userRent);


}
