package cn.iocoder.yudao.module.game.controller.console.dst;


import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.game.common.ResultData;
import cn.iocoder.yudao.module.game.common.enums.dst.DstEmoji;
import cn.iocoder.yudao.module.game.common.enums.dst.GameMode;
import cn.iocoder.yudao.module.game.common.enums.dst.LogType;
import cn.iocoder.yudao.module.game.common.enums.dst.PlayerFileType;
import cn.iocoder.yudao.module.game.common.enums.dst.WorldType;
import cn.iocoder.yudao.module.game.common.params.PlayManageParam;
import cn.iocoder.yudao.module.game.common.params.UpdatePlayerFileParams;
import cn.iocoder.yudao.module.game.common.params.WorldLevelOverrideParam;
import cn.iocoder.yudao.module.game.common.response.*;
import cn.iocoder.yudao.module.game.dal.dataobject.dstblockmod.DstBlockModDO;
import cn.iocoder.yudao.module.game.dal.mysql.dstblockmod.DstBlockModMapper;
import cn.iocoder.yudao.module.game.service.console.dst.ICommonService;
import cn.iocoder.yudao.module.game.service.console.dst.IDstService;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ArchiveFileInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LevelConfig;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ModOverride;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.Player;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ProcessInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.SessionMeta;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.SteamModInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@Tag(name = "集群设置(房间设置)")
@RequestMapping("/console/dst")
public class DontStarveController {

    @Autowired
    IDstService dstService;

    @Autowired
    DstBlockModMapper blockModDao;

    @Autowired
    ICommonService commonService;

//    @Autowired
//    MBTDicDao dicDao;

    @Operation(summary = "获取房间信息(名称/模式/进度/直连代码/饥荒版本)")
    @GetMapping("/get-worldinfo")
    public ResultData getWorldInfo() {
        WorldInfoResponse worldInfo = dstService.getWorldInfo();
        return ResultData.withDataSuccess(worldInfo);
    }

    @Operation(summary = "获取当前集群的配置(cluster.ini)")
    @GetMapping("/cluster/config/get")
    public ResultData readClusterConfig() {
        ClusterConfig clusterInfo = dstService.getClusterConfig();
        return ResultData.withDataSuccess(clusterInfo);
    }

    @Operation(summary = "保存集群配置(cluster.ini)")
    @PostMapping("/cluster/config/save")
    public ResultData saveClusterConfig(@RequestBody ClusterConfig clusterConfig
    ) {
        dstService.saveClusterConfig(clusterConfig);
        commonService.saveOperateLog("保存【房间设置】信息");

        return ResultData.success();
    }

    @Operation(summary = "开启/关闭/查询集群状态", description = "参考ClusterStatusOperation.Status")
    @PostMapping("/cluster/status-operation")
    public ResultData operateClusterStatus(@RequestBody ClusterStatusOperation operation) {
        Object response = dstService.operateStatus(operation);
        if (operation.getStatus() != ClusterStatusOperation.Status.QUERY_STATUS) {
            commonService.saveOperateLog("世界状态-" + operation.getStatus().name());
        }

        return ResultData.withDataSuccess(response);
    }

    @Operation(summary = "列出在线玩家")
    @GetMapping("/cluster/players/list")
    public ResultData listPlayers() {
        List<Player> players = dstService.listPlayers();
        return ResultData.withDataSuccess(players);
    }

    @Operation(summary = "读取玩家文件(管理员/黑名单/白名单)")
    @GetMapping("/cluster/players/read-file")
    public ResultData readPlayerFile(@RequestParam PlayerFileType type) {
        String content = dstService.readPlayerFiles(type);
        return ResultData.withDataSuccess(content);
    }

    @Operation(summary = "update玩家文件(管理员/黑名单/白名单)")
    @PostMapping("/cluster/players/update-file")
    public ResultData updatePlayerFile(@RequestBody UpdatePlayerFileParams params) {
        String content = dstService.updatePlayerFiles(params.getType(), params.getContent());
        commonService.saveOperateLog("保存玩家权限配置文件(管/黑/白)");
        return ResultData.withDataSuccess(content);
    }


    @Operation(summary = "获取日志")
    @GetMapping("/cluster/logs")
    public ResultData getLogs(
            @RequestParam LogType logType,
            @RequestParam Integer line) {
        String logContent = dstService.getLogs(logType, line);
        return ResultData.withDataSuccessNoEncrypt(logContent);
    }

    @Operation(summary = "获取错误日志/检测报错/检查报错")
    @GetMapping("/cluster/trackServerLog")
    @Deprecated
    public ResultData trackServerLog(@RequestParam LogType logType) {
        List<String> logContent = dstService.trackServerLog(logType);
        return ResultData.withDataSuccessNoEncrypt(logContent);
    }

    @Operation(summary = "获取错误日志/检测报错/检查报错")
    @GetMapping("/cluster/trackServerLogV2")
    public ResultData trackServerLogV2(@RequestParam LogType logType) {
        TrackServerLogResponse trackServerLogResponse = dstService.trackServerLogV2(logType);
        return ResultData.withDataSuccessNoEncrypt(trackServerLogResponse);
    }

    @Deprecated
    @Operation(summary = "重启世界(没用到)", description = "丢弃当前这一个存档并重启世界(恢复到最近一个存档的位置)")
    @GetMapping("/cluster/restart")
    public ResultData resetCluster() {
        dstService.restartClusterWithoutSave();
        return ResultData.success();
    }

//    @Deprecated
//    @Operation(summary = "重新生成世界/重置世界", description = "放弃现在的存档，重新生成世界(如果更改了默认世界配置，将会启用新的世界配置)")
//    @GetMapping("/cluster/reGenerate")
//    public ResultData reGenerateCluster() {
//        dstService.reGenerateClusterWithoutSave();
//        return ResultData.success();
//    }

    @Operation(summary = "通过删除save目录的方式强制重置世界(1)", description = "放弃现在的存档，重新生成世界(如果更改了默认世界配置，将会启用新的世界配置)")
    @GetMapping("/cluster/forceReGenerateWorld")
    public ResultData forceReGenerateWorld() {
        dstService.forceReGenerateWorld();
        commonService.saveOperateLog("重置世界");
        return ResultData.success();
    }

//    @Deprecated
//    @Operation(summary = "通过覆盖存档的方式强制重置世界(2)", description = "放弃现在的存档，重新生成世界(如果更改了默认世界配置，将会启用新的世界配置)")
//    @GetMapping("/cluster/forceReGenerateWorld2")
//    public ResultData forceReGenerateWorld2() {
//        dstService.forceReGenerateWorld2();
//        return ResultData.success();
//    }

    @Operation(summary = "回档", description = "")
    @GetMapping("/cluster/override-archive")
    public ResultData overrideArchive(@RequestParam(required = true, defaultValue = "1") Integer day) {
        WorldInfoResponse worldInfo = dstService.getWorldInfo();
        String currentDay = worldInfo.getCurrentDay();
        List<SessionMeta> sessionMetas = dstService.listSessionMetaFiles();
        String toDay = "";
        for (SessionMeta sessionMeta : sessionMetas) {
            if (Objects.equals(sessionMeta.getIndex(), day)) {
                toDay = sessionMeta.getCurrentDay();
            }
        }
        dstService.overrideArchive(day);
        String name = "回档:当前" + currentDay + ", 选择回档第" + day + "个, 要回到" + toDay;
        commonService.saveOperateLog(name);
        return ResultData.success();
    }


    @Operation(summary = "从steam查询Mod", description = "")
    @GetMapping("/search-mod")
    public ResultData searchMod(@RequestParam(required = true) String searchContent) {
        List<SteamModInfo> steamModInfo = dstService.searchMod(searchContent);
        commonService.saveOperateLog("搜索模组内容:" + searchContent);
        return ResultData.withDataSuccess(steamModInfo);
    }

    @Operation(summary = "检查模组是否在黑名单中", description = "")
    @GetMapping("/check-block-mod")
    public ResultData checkBlockMod(@RequestParam(required = true) String modId) {
        DstBlockModDO mbtBlockModEntity = blockModDao.selectOne(new LambdaQueryWrapperX<DstBlockModDO>().eq(DstBlockModDO::getModId, Long.parseLong(modId)).last("limit 1"));

        BlockModResponse response = new BlockModResponse();
        response.setStatus(null);
        if (mbtBlockModEntity != null) {
            response.setStatus(mbtBlockModEntity.getStatus());
            response.setModId(modId);
            response.setReason(mbtBlockModEntity.getDesc());
        }
        return ResultData.withDataSuccess(response);
    }

    @Deprecated
    @Operation(summary = "订阅模组", description = "")
    @GetMapping("/subscribe-mod")
    public ResultData subscribeMod(@RequestParam(required = true) String modId) {
        LocalModInfo localModInfo = dstService.subscribeMod(modId);
        commonService.saveOperateLog("订阅模组:" + modId + "【" + localModInfo.getTitle() + "】");
        return ResultData.withDataSuccess(localModInfo);
    }

    @Operation(summary = "订阅模组version2", description = "")
    @GetMapping("/subscribe-mod/version2")
    public ResultData subscribeModVersion2(@RequestParam(required = true) String modId) {
        LocalModInfo localModInfo = dstService.subscribeModVersion2(modId);
        commonService.saveOperateLog("订阅模组version2:" + modId + "【" + localModInfo.getTitle() + "】");
        return ResultData.withDataSuccess(localModInfo);
    }

    // !!! 和subscribeModVersion2共用一套代码.
    @Operation(summary = "重新下载某个模组", description = "")
    @GetMapping("/redownload-mod")
    public ResultData redownloadMod(@RequestParam(required = true) String modId) {
        dstService.deleteMod(modId);
        LocalModInfo localModInfo = dstService.subscribeModVersion2(modId);
        commonService.saveOperateLog("重新下载模组:" + modId + "【" + localModInfo.getTitle() + "】");
        return ResultData.withDataSuccess(localModInfo);
    }

    // 清理acf文件并重新下载mod.
    @Operation(summary = "订阅失败后, 弹窗提示“重新下载的按钮”，清理acf文件并重新下载mod", description = "")
    @GetMapping("/flushAcfThenReDownloadMod")
    public ResultData flushAcfThenReDownloadMod(@RequestParam(required = true) String modId) {
        dstService.deleteMod(modId);
        // 清理acf文件
        dstService.flushSteamAcfFile(modId);
        LocalModInfo localModInfo = dstService.subscribeModVersion2(modId);
        commonService.saveOperateLog("刷新acf并重新下载模组:" + modId + "【" + localModInfo.getTitle() + "】");
        return ResultData.withDataSuccess(localModInfo);
    }

    @Operation(summary = "取消订阅模组", description = "")
    @GetMapping("/unsubscribe-mod")
    public ResultData unSubscribeMod(@RequestParam(required = true) String modId) {
        dstService.deleteMod(modId);
        commonService.saveOperateLog("取消订阅模组:" + modId);
        return ResultData.success();
    }

    @Operation(summary = "获取本地Mod以及相关配置项", description = "")
    @GetMapping("/local-mods")
    public ResultData getLocalMods() {
        List<LocalModInfo> localMods = dstService.getLocalMods();
        return ResultData.withDataSuccess(localMods);
    }

    @Operation(summary = "清除mod缓存", description = "")
    @GetMapping("/clear-mod-cache")
    public ResultData clearModCache() {
        dstService.clearModCache();
        commonService.saveOperateLog("清除mod缓存");
        return ResultData.success();
    }

    @Deprecated
    @Operation(summary = "获取当前存档的模组配置", description = "")
    @GetMapping("/current-modoverride")
    public ResultData getCurrentMods() {
        List<ModOverride> currentMods = dstService.getCurrentMods();
        return ResultData.withDataSuccess(currentMods);
    }


    @Operation(summary = "保存当前存档的模组配置", description = "")
    @PostMapping("/save-modoverride")
    public ResultData saveModOverride(@RequestBody List<ModOverride> mods) {
        dstService.saveModOverrideVersion2(mods);
        commonService.saveOperateLog("保存modoverrides.lua");
        return ResultData.success();
    }

    @Operation(summary = "获取当前的世界配置", description = "")
    @GetMapping("/get-worldoverride")
    public ResultData getWorldOverride(@RequestParam WorldType worldType) {
        LevelConfig levelOverride = dstService.getLevelOverride(worldType);
        return ResultData.withDataSuccess(levelOverride);
    }

    @Operation(summary = "获取特定模式的推荐值(包含生存/无尽等模式, 也可以传入我们自己定义的模式)", description = "")
    @GetMapping("/getWorldOptionWithMode")
    public ResultData getWorldOptionWithMode(@RequestParam(required = false) WorldType worldType, @RequestParam GameMode mode) {
        List<WorldOptionWithMode> worldOptionWithMode = dstService.getWorldOptionWithMode(worldType, mode);
        return ResultData.withDataSuccess(worldOptionWithMode);
    }

    @Operation(summary = "直接设置游戏模式(会覆盖一些选项,非预设的选项不会变动, 仅涉及master世界)", description = "")
    @GetMapping("/saveWorldOptionWithMode")
    public ResultData saveWorldOptionWithMode(@RequestParam(required = false) WorldType worldType, @RequestParam GameMode mode) {
        if (worldType == null) {
            worldType = WorldType.master;
        }
        dstService.saveWorldOptionWithMode(worldType, mode);
        return ResultData.success();
    }


    @Operation(summary = "保存当前的世界配置", description = "")
    @PostMapping("/save-worldoverride")
    public ResultData saveWorldOverride(@RequestBody WorldLevelOverrideParam param) {
        dstService.saveLevelOverride(param);
        commonService.saveOperateLog("保存【" + param.getWorldType().name() + "】世界选项leveldataoverride.lua");
        return ResultData.success();
    }

    @Operation(summary = "让某玩家更换角色")
    @GetMapping("/change-character")
    public ResultData changeCharacter(@RequestParam String number) {
        dstService.rePickCharacter(number);
        return ResultData.success();
    }

    @Operation(summary = "管理游戏内玩家")
    @PostMapping("/manage-player")
    public ResultData managePlayer(@RequestBody PlayManageParam param) {
        dstService.managePlayer(param);
        return ResultData.success();
    }

    @Operation(summary = "发布公告")
    @PostMapping("/announcement")
    public ResultData announcement(@RequestParam String content) {
        dstService.announcement(content);
        return ResultData.success();
    }


    @Operation(summary = "获取当前服务器进程信息")
    @GetMapping("/get-processinfo")
    public ResultData getProcessInfo() {
        ProcessInfo processInfo = dstService.getCurrentProcessInfo();
        return ResultData.withDataSuccess(processInfo);
    }

    @Operation(summary = "获取当前存档文件列表")
    @GetMapping("/get-archive-list")
    public ResultData getArchives() {
        List<ArchiveFileInfo> archiveList = dstService.getArchiveList();
        return ResultData.withDataSuccess(archiveList);
    }

    @Operation(summary = "重命名存档文件")
    @GetMapping("/rename-archive")
    public ResultData renameArchive(@RequestParam String fileName, @RequestParam String newFileName) {
        dstService.renameArchive(fileName, newFileName);
        return ResultData.success();
    }

    @Operation(summary = "删除存档文件")
    @GetMapping("/remove-archive")
    public ResultData removeArchive(@RequestParam String fileName) {
        dstService.removeArchive(fileName);
        commonService.saveOperateLog("删除存档文件:" + fileName);
        return ResultData.success();
    }

    @Operation(summary = "创建存档压缩文件")
    @GetMapping("/zip-archive")
    public ResultData zipArchive() {
        dstService.zipArchive();
        return ResultData.success();
    }

    @Operation(summary = "解压存档文件")
    @GetMapping("/unzip-archive")
    public ResultData unzipArchive(@RequestParam String fileName) {
        dstService.unzipArchive(fileName);
        commonService.saveOperateLog("恢复存档:" + fileName);
        return ResultData.success();
    }

    @Operation(summary = "获取当前存档的模组配置文件中的模组ID列表")
    @GetMapping("/getCurrentModOverrideModIds")
    public ResultData getCurrentModOverrideModIds() {
        List<String> currentModOverrides = dstService.getCurrentModOverrides();
        return ResultData.withDataSuccess(currentModOverrides);
    }

    @Operation(summary = "将当前存档配置文件中的模组ID, 全部刷入到dedicated_server_mods_setup.lua")
    @GetMapping("/flushModsIdIntoSetupFile")
    public ResultData flushModsIdIntoSetupFile() {
        dstService.flushModsIdIntoSetupFile();
        return ResultData.success();
    }

    @Operation(summary = "获取agent url, 用于下载/上传存档.")
    @GetMapping("/get-agent-url")
    public ResultData getAgentUrl() {
        AgentInfo agentInfo = dstService.getAgentUrl();
        return ResultData.withDataSuccess(agentInfo);
    }

    @Operation(summary = "更新游戏.")
    @GetMapping("/update-game")
    public ResultData updateGame() {
        dstService.updateGame();
        return ResultData.success();
    }

    @Operation(summary = "查询emoji.")
    @GetMapping("/emojis")
    public ResultData emojis() {
        DstEmoji emoji = new DstEmoji();
        return ResultData.withDataSuccess(emoji.getItems());
    }

    @Deprecated
    @Operation(summary = "列出所有存档节点.")
    @GetMapping("/list-all-metas")
    public ResultData listSessionMetaFiles() {
        List<SessionMeta> sessionMetas = dstService.listSessionMetaFiles();
        return ResultData.withDataSuccess(sessionMetas);
    }

    @Operation(summary = "更新所有本地模组.")
    @GetMapping("/update-all-mods")
    public ResultData updateAllMods() {
//        dstService.flushModsIdIntoSetupFile();
        dstService.updateAllMods();
        return ResultData.success();
    }

    @Operation(summary = "保存进度.")
    @GetMapping("/save-current-world")
    public ResultData saveCurrentWorld() {
        dstService.saveCurrentWorld();
        return ResultData.success();
    }

    @Operation(summary = "获取饥荒最新版本")
    @GetMapping("/dst-latest-version")
    public ResultData getDSTLatestVersion() {
        DstGameInfo version = dstService.getLatestVersion();
        return ResultData.withDataSuccess(version);
    }

    @Operation(summary = "从steam检查模组是否更新.")
    @GetMapping("/checkModSteamLatestVersionNow")
    public ResultData checkModSteamLatestVersionNow(@RequestParam String modId) {
        dstService.checkModSteamLatestVersionNow(modId);
        return ResultData.success();
    }


}
