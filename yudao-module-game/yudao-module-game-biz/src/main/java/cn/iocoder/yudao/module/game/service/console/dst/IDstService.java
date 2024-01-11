package cn.iocoder.yudao.module.game.service.console.dst;

import cn.iocoder.yudao.module.game.common.enums.dst.GameMode;
import cn.iocoder.yudao.module.game.common.enums.dst.LogType;
import cn.iocoder.yudao.module.game.common.enums.dst.PlayerFileType;
import cn.iocoder.yudao.module.game.common.enums.dst.WorldType;
import cn.iocoder.yudao.module.game.common.params.PlayManageParam;
import cn.iocoder.yudao.module.game.common.params.WorldLevelOverrideParam;
import cn.iocoder.yudao.module.game.common.response.AgentInfo;
import cn.iocoder.yudao.module.game.common.response.ClusterConfig;
import cn.iocoder.yudao.module.game.common.response.ClusterStatusOperation;
import cn.iocoder.yudao.module.game.common.response.DstGameInfo;
import cn.iocoder.yudao.module.game.common.response.TrackServerLogResponse;
import cn.iocoder.yudao.module.game.common.response.WorldInfoResponse;
import cn.iocoder.yudao.module.game.common.response.WorldOptionWithMode;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ArchiveFileInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LevelConfig;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ModOverride;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.Player;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ProcessInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.SessionMeta;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.SteamModInfo;

import java.util.List;

public interface IDstService {

    ClusterConfig getClusterConfig();

    void saveClusterConfig(ClusterConfig config);

    WorldInfoResponse getWorldInfo();

    /**
     * 操作集群状态.
     */
    Object operateStatus(ClusterStatusOperation operation);

    void setClusterEqualPort(InstanceDO instance);

    // 强制某玩家重选角色
    void rePickCharacter(String number);

    void managePlayer(PlayManageParam param);

    void announcement(String content);

    // 丢弃当前这一个存档并重启世界
    void restartClusterWithoutSave();

    // 丢弃当前全部存档并重新生成世界
    @Deprecated
    void reGenerateClusterWithoutSave();

    // 强制重置世界, 删除相关文件,
    void forceReGenerateWorld();

    // 强制重置世界, 使用默认存档文件覆盖.
    @Deprecated
    void forceReGenerateWorld2();

    List<Player> listPlayers();

    // 读取admin/block/等文件内容
    String readPlayerFiles(PlayerFileType type);

    String updatePlayerFiles(PlayerFileType type, String content);

    // 回档
    void overrideArchive(Integer day);

    // 立即存档/保存进度
    void saveCurrentWorld();

    // 获取日志
    String getLogs(LogType logType, Integer line);

    List<String> trackServerLog(LogType logType);

    TrackServerLogResponse trackServerLogV2(LogType logType);

    // 从steam 查询模组信息返回给前端.
    List<SteamModInfo> searchMod(String searchContent);

    // 获取本地已有的模组。同时返回存档已配置模组的配置信息, 如果本地没有该模组, 则自动触发下载.
    List<LocalModInfo> getLocalMods();

    void clearModCache();

    // 订阅模组
    @Deprecated
    LocalModInfo subscribeMod(String mod);

    // 订阅模组version2/重新下载某个模组
    LocalModInfo subscribeModVersion2(String mod);

    // 从acf文件中清理某个Mod
    void flushSteamAcfFile(String mod);

    // 从服务器的模组目录 删除模组
    void deleteMod(String mod);

    // 用于定时任务更新模组信息.
    void updateDbTimer();

    // 获取当前已配置的模组 TODO 模组这一部分的逻辑需要好好优化下, 属于核心功能.
    List<ModOverride> getCurrentMods();

    // 保存模组配置
    void saveModOverride(List<ModOverride> mods);

    void saveModOverrideVersion2(List<ModOverride> mods);

    LevelConfig getLevelOverride(WorldType worldType);

    void saveLevelOverride(WorldLevelOverrideParam param);

    ProcessInfo getCurrentProcessInfo();

    List<ArchiveFileInfo> getArchiveList();

    void renameArchive(String fileName, String newFileName);

    void zipArchive();

    void unzipArchive(String fileName);

    List<String> getCurrentModOverrides();

    void flushModsIdIntoSetupFile();

    void removeArchive(String fileName);

    AgentInfo getAgentUrl();

    void updateGame();

    // 这个更新没有用, 更换了模组下载的方式.
//    @Deprecated
    void updateAllMods();

    List<SessionMeta> listSessionMetaFiles();

    List<WorldOptionWithMode> getWorldOptionWithMode(WorldType worldType, GameMode mode);

    void saveWorldOptionWithMode(WorldType worldType, GameMode mode);

    void checkModSteamLatestVersionNow(String modId);

    DstGameInfo getLatestVersion();
}
