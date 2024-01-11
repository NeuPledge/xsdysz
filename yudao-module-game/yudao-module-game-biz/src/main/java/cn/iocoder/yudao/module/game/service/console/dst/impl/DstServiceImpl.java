package cn.iocoder.yudao.module.game.service.console.dst.impl;

import cn.hutool.core.date.StopWatch;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.dstmods.DstModsDO;
import cn.iocoder.yudao.module.game.dal.dataobject.dstworldoption.DstWorldOptionDO;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;
import cn.iocoder.yudao.module.game.dal.mysql.dstmods.DstModsMapper;
import cn.iocoder.yudao.module.game.dal.mysql.dstworldoption.DstWorldOptionMapper;
import cn.iocoder.yudao.module.game.dal.mysql.instance.InstanceMapper;
import cn.iocoder.yudao.module.game.dal.mysql.rent.RentMapper;
import cn.iocoder.yudao.module.game.service.console.clients.dst.DstClient;
import cn.iocoder.yudao.module.game.service.console.dst.ICommonService;
import cn.iocoder.yudao.module.game.service.console.dst.IDstService;
import cn.iocoder.yudao.module.game.service.console.dst.impl.dst.ModService;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.iocoder.yudao.module.game.common.consts.AppException;
import cn.iocoder.yudao.module.game.common.consts.BusinessException;
import cn.iocoder.yudao.module.game.common.enums.dst.DayPhase;
import cn.iocoder.yudao.module.game.common.enums.dst.GameMode;
import cn.iocoder.yudao.module.game.common.enums.dst.LogType;
import cn.iocoder.yudao.module.game.common.enums.dst.PlayerFileType;
import cn.iocoder.yudao.module.game.common.enums.dst.Season;
import cn.iocoder.yudao.module.game.common.enums.dst.WorldType;
import cn.iocoder.yudao.module.game.common.params.PlayManageParam;
import cn.iocoder.yudao.module.game.common.params.WorldLevelOverrideParam;
import cn.iocoder.yudao.module.game.common.params.WorldLevelOverrideParam.WorldOverride;
import cn.iocoder.yudao.module.game.common.response.AgentInfo;
import cn.iocoder.yudao.module.game.common.response.ClusterConfig;
import cn.iocoder.yudao.module.game.common.response.ClusterStatusOperation;
import cn.iocoder.yudao.module.game.common.response.DstGameInfo;
import cn.iocoder.yudao.module.game.common.response.TrackServerLogResponse;
import cn.iocoder.yudao.module.game.common.response.WorldInfoResponse;
import cn.iocoder.yudao.module.game.common.response.WorldOptionWithMode;
import cn.iocoder.yudao.module.game.framework.HeaderHolder;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ArchiveFileInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ClusterIni;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LevelConfig;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LevelConfig.LevelConfigItem;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LevelConfig.LevelOption;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo.ModConfig;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo.ModConfig.Option;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ModOverride;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.Player;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ProcessInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.ProcessInfo.ProcessItem;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.SessionMeta;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.SteamModInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.WorldInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.WorldStatus;
import cn.iocoder.yudao.module.game.util.Assert;
import cn.iocoder.yudao.module.game.util.DateFormatUtil;
import cn.iocoder.yudao.module.game.util.Shell;
import cn.iocoder.yudao.module.game.util.SteamUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.module.game.common.consts.Constant.HEADER_RENT_ID;

@Slf4j
@Service
public class DstServiceImpl implements IDstService {

    @Autowired
    RentMapper userRentDao;

    @Autowired
    DstModsMapper dstModDao;

    @Autowired
    DstWorldOptionMapper worldOptionDao;

    @Autowired
    InstanceMapper instanceDao;

    @Autowired
    DictDataApi dictDataApi;

    @Autowired
    ModService modService;

    @Autowired
    ICommonService commonService;

    @Override
    public List<Player> listPlayers() {
        DstClient client = DstClient.getInstance();
        InstanceDO instance = getCurrentUserRentInstance();
        List<Player> onlinePlayers = client.getOnlinePlayers(instance);
        return onlinePlayers;
    }

    private RentDO getCurrentUserRentInfo() {
        Long userId = HeaderHolder.getCurrentUserId();
        RentDO rentEntity = userRentDao.selectOne(new LambdaQueryWrapperX<RentDO>().eq(RentDO::getUserId, userId));

        MDC.put(HEADER_RENT_ID, rentEntity.getId().toString());
        return rentEntity;
    }

    // 获取当前用户租约相关联的机器实例.
    private InstanceDO getCurrentUserRentInstance() {
        Long userId = HeaderHolder.getCurrentUserId();
        RentDO rentEntity = userRentDao.selectOne(new LambdaQueryWrapperX<RentDO>().eq(RentDO::getUserId, userId));

        MDC.put(HEADER_RENT_ID, rentEntity.getId().toString());
        InstanceDO instance = instanceDao.selectById(rentEntity.getInstanceId());
        return instance;
    }


    @Override
    public Object operateStatus(ClusterStatusOperation operation) {
        if (operation.getStatus() == null) {
            throw new AppException("状态错误.");
        }
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();

        switch (operation.getStatus()) {
            case QUERY_STATUS -> {
                try {
                    WorldStatus worldStatus = client.queryWorldsStatus(instance);
                    return worldStatus;
                } catch (Exception exception) {
                    commonService.forceStartAgent(instance);
                    WorldStatus worldStatus = client.queryWorldsStatus(instance);
                    return worldStatus;
                }
            }
            case START_ALL -> {
                startMaster(client, instance);
                startCaves(client, instance);
            }
            case STOP_ALL -> {
                client.stopMaster(instance);
                client.stopCaves(instance);
            }
            case START_MASTER -> startMaster(client, instance);
            case STOP_MASTER -> client.stopMaster(instance);
            case START_CAVES -> startCaves(client, instance);
            case STOP_CAVES -> client.stopCaves(instance);
        }
        return null;
    }

    @Override
    public void setClusterEqualPort(InstanceDO instance) {

        ClusterConfig clusterConfig = getClusterConfig();
        if (clusterConfig.getClusterPort() != null && !clusterConfig.getClusterPort().equals(instance.getPortDstCluster())) {
            clusterConfig.setClusterPort(instance.getPortDstCluster());
            saveClusterConfig(clusterConfig);
            log.info("instanceId:{} 集群已设置对等端口:{}", instance.getId(), instance.getPortDstCluster());
        }

    }


    private void startMaster(DstClient client, InstanceDO instance) {
        if ((instance.getCompanyCode().contains("zhidian") || instance.getCompanyCode().equals("h1")) && instance.getPortDstMaster() != null) {
            if (instance.getId() > 530) {
                commonService.setMasterEqualPort(instance);
                setClusterEqualPort(instance);
            } else {
                try {
                    DictDataRespDTO mbtDic = dictDataApi.parseDictData("equal_port_hosts", "equal_port_hosts");
                    String hosts = mbtDic.getValue();
                    if (StringUtils.hasText(hosts)) {
                        List<Long> instIds = JSON.parseArray(hosts, Long.class);
                        if (instIds.contains(instance.getId())) {
                            commonService.setMasterEqualPort(instance);
                            setClusterEqualPort(instance);
                        }
                    }
                } catch (Exception exception) {
                    log.error("设置对等端口遇到错误");
                    // do nothing
                }

            }
        }
        client.startMaster(instance);
    }

    private void startCaves(DstClient client, InstanceDO instance) {
        ClusterIni clusterIni = DstClient.getInstance().getClusterIni(instance);
        if (clusterIni.getSHARD().getShard_enabled() == false) {
            log.error("此存档未开启 shard_enabled, 无法启动洞穴");
            return;
        }
//        if ((currentUserRentInfo.getUserId() > 680 || currentUserRentInfo.getUserId() < 20) && currentUserRentInfo.getPackageId() != null && currentUserRentInfo.getPackageId() == 2) {
//            log.error("特价版限制启动洞穴");
//            return;
//        }
        if ((instance.getCompanyCode().contains("zhidian") || instance.getCompanyCode().equals("h1")) && instance.getPortDstMaster() != null) {
            if (instance.getId() > 530) {
                commonService.setCavesEqualPort(instance);
                setClusterEqualPort(instance);
            } else {
                try {
//                    MBTDicEntityExample example = new MBTDicEntityExample();
//                    MBTDicEntityExample.Criteria criteria = example.createCriteria();
//                    criteria.andTypeEqualTo("指点云已配置对等端口的机器ID");
//                    criteria.andKeyEqualTo("equal_port_hosts");
//                    MBTDicEntity mbtDic = dictDataApi.selectOneByExample(example);
//                    String hosts = mbtDic.getValue();

                    DictDataRespDTO mbtDic = dictDataApi.parseDictData("equal_port_hosts", "equal_port_hosts");
                    String hosts = mbtDic.getValue();

                    if (StringUtils.hasText(hosts)) {
                        List<Long> instIds = JSON.parseArray(hosts, Long.class);
                        if (instIds.contains(instance.getId())) {
                            commonService.setCavesEqualPort(instance);
                            setClusterEqualPort(instance);
                        }
                    }
                } catch (Exception exception) {
                    log.error("设置对等端口遇到错误");
                    // do nothing
                }

            }
        }
        client.startCaves(instance);
    }

    @Override
    public WorldInfoResponse getWorldInfo() {
        Long userId = HeaderHolder.getCurrentUserId();
        RentDO rentEntity = userRentDao.selectOne(new LambdaQueryWrapperX<RentDO>().eq(RentDO::getUserId, userId));


        MDC.put(HEADER_RENT_ID, rentEntity.getId().toString());
        InstanceDO instance = instanceDao.selectById(rentEntity.getInstanceId());

//        InstanceDO instance = getCurrentUserRentInstance();

        WorldInfo worldInfo = DstClient.getInstance().getWorldInfo(instance);
        String dstVersion = DstClient.getInstance().getDstVersion(instance);
//        MasterServerIni masterIni = DstClient.getInstance().getMasterIni(instance);
//        String serverPort = masterIni.getNETWORK().getServer_port();

        ClusterIni clusterIni = DstClient.getInstance().getClusterIni(instance);

        WorldInfoResponse worldInfoResponse = new WorldInfoResponse();
        worldInfoResponse.setRoomName(clusterIni.getNETWORK().getCluster_name());
//        worldInfoResponse.setGameIntention(GameIntention.valueOf(clusterIni.getNETWORK().getCluster_intention()).getName());
        try {
            worldInfoResponse.setGameMode(GameMode.valueOf(clusterIni.getGAMEPLAY().getGame_mode()).getName());
        } catch (Exception exception) {
            worldInfoResponse.setGameMode("无法识别");
        }
        if (worldInfo.getClock() != null) {
            worldInfoResponse.setCycles(worldInfo.getClock().getCycles());
            worldInfoResponse.setCycles(worldInfo.getClock().getCycles() + 1);
            worldInfoResponse.setCurrentDay(String.format("第%d天", worldInfo.getClock().getCycles() + 1));
            worldInfoResponse.setPhase(worldInfo.getClock().getPhase());
            worldInfoResponse.setPhase_zh(DayPhase.valueOf(worldInfo.getClock().getPhase()).getName());
        }
        if (worldInfo.getSeasons() != null) {
            worldInfoResponse.setSeason(Season.valueOf(worldInfo.getSeasons().getSeason()).getName());
            worldInfoResponse.setPhaseRate(DstClient.calculateDayRemains(worldInfo) + "%");
        }

        String roomPwd = clusterIni.getNETWORK().getCluster_password();
        worldInfoResponse.setConsoleConnect(String.format("c_connect(\"%s\", %d,\"%s\")", instance.getIp(), instance.getPortDstMaster(), roomPwd));
        worldInfoResponse.setRoomPassword(roomPwd);
        worldInfoResponse.setVersion(dstVersion);

        if (StringUtils.hasText(dstVersion) && StringUtils.hasText(dstVersion.trim())) {
            instance.setGameVersionDst(dstVersion);
            instanceDao.updateById(instance);
        }
        try {
            if (clusterIni.getSHARD().getShard_enabled() == false) {
                worldInfoResponse.setShardEnabled(0);// 未开启洞穴
            } else {
                if ((userId > 680 || userId < 20) && rentEntity.getPackageId() != null && rentEntity.getPackageId() == 2) {// 从用户id 680开始限制特价版禁止启动洞穴
                    worldInfoResponse.setShardEnabled(1);// 特价版禁止启动洞穴
                } else {
                    worldInfoResponse.setShardEnabled(1);
                }
            }
        } catch (Exception exception) {
            worldInfoResponse.setShardEnabled(1);
        }
        return worldInfoResponse;
    }


    @Override
    public ClusterConfig getClusterConfig() {
        InstanceDO instance = getCurrentUserRentInstance();

        RentDO rentEntity = getCurrentUserRentInfo();

        ClusterIni clusterIni = DstClient.getInstance().getClusterIni(instance);
        ClusterConfig clusterConfig = new ClusterConfig();
        clusterConfig.setRoomName(clusterIni.getNETWORK().getCluster_name());
//        clusterConfig.setGameIntention(clusterIni.getNETWORK().getCluster_intention());
//        clusterConfig.setGameIntentionStr(GameIntention.valueOf(clusterIni.getNETWORK().getCluster_intention()).getName());
        clusterConfig.setRoomPassword(clusterIni.getNETWORK().getCluster_password());
        clusterConfig.setRoomDescription(clusterIni.getNETWORK().getCluster_description());
        clusterConfig.setLanOnlyCluster(clusterIni.getNETWORK().getLan_only_cluster());
        clusterConfig.setGameMode(clusterIni.getGAMEPLAY().getGame_mode());
        clusterConfig.setGameModeStr(GameMode.valueOf(clusterIni.getGAMEPLAY().getGame_mode()).getName());
        clusterConfig.setMaxPlayers(clusterIni.getGAMEPLAY().getMax_players());
        clusterConfig.setPvp(clusterIni.getGAMEPLAY().getPvp());
        clusterConfig.setMaxPlayersLimit(rentEntity.getPlayerLimit());
        clusterConfig.setPauseWhenEmpty(clusterIni.getGAMEPLAY().getPause_when_empty());
        clusterConfig.setWhitelistSlots(clusterIni.getNETWORK().getWhitelist_slots() == null ? 0 : clusterIni.getNETWORK().getWhitelist_slots());

        clusterConfig.setSteamGroupEnabled(false);
        clusterConfig.setSteamGroupOnly(true);
        clusterConfig.setSteamGroupAdmins(true);
        clusterConfig.setSteamGroupId(null);
        if (clusterIni.getSTEAM() != null) {
            clusterConfig.setSteamGroupEnabled(true);
            clusterConfig.setSteamGroupOnly(clusterIni.getSTEAM().getSteam_group_only());
            clusterConfig.setSteamGroupAdmins(clusterIni.getSTEAM().getSteam_group_admins());
            clusterConfig.setSteamGroupId(clusterIni.getSTEAM().getSteam_group_id());
        }
        clusterConfig.setClusterPort(Integer.parseInt(clusterIni.getSHARD().getMaster_port()));

        return clusterConfig;
    }

    @Override
    public void saveClusterConfig(ClusterConfig config) {
        Long userId = HeaderHolder.getCurrentUserId();

        RentDO rentEntity = userRentDao.selectOne(new LambdaQueryWrapperX<RentDO>().eq(RentDO::getUserId, userId));

        if (config.getMaxPlayers() > rentEntity.getPlayerLimit()) {
//            throw new BusinessException("玩家人数超过套餐上限");
            config.setMaxPlayersLimit(rentEntity.getPlayerLimit());
        }
        InstanceDO instance = getCurrentUserRentInstance();
        if (config.getLanOnlyCluster() == null || config.getLanOnlyCluster() == true) {
            config.setLanOnlyCluster(false);
        }
        DstClient.getInstance().updateClusterIni(instance, config);
    }

    @Override
    public String readPlayerFiles(PlayerFileType type) {
        InstanceDO instance = getCurrentUserRentInstance();
        String content = DstClient.getInstance().readPlayerFiles(instance, type.name());
        return content;
    }

    @Override
    public String updatePlayerFiles(PlayerFileType type, String content) {
        InstanceDO instance = getCurrentUserRentInstance();

        DstClient.getInstance().updatePlayerFiles(instance, type.name(), content);

        // 更新后，重新拉取一遍
        content = DstClient.getInstance().readPlayerFiles(instance, type.name());

        return content;
    }


    @Override
    public String getLogs(LogType logType, Integer line) {
        InstanceDO instance = getCurrentUserRentInstance();

        String logContent = DstClient.getInstance().getLog(instance, logType, line);
        return logContent;
    }

    @Override
    public List<String> trackServerLog(LogType logType) {
        InstanceDO instance = getCurrentUserRentInstance();
        List<String> logContent = DstClient.getInstance().trackServerLog(instance, logType);
        if (CollectionUtils.isEmpty(logContent)) {
            logContent = new ArrayList<>();
        }
        return logContent;
    }

    @Override
    public TrackServerLogResponse trackServerLogV2(LogType logType) {
        InstanceDO instance = getCurrentUserRentInstance();
        List<String> logContent = DstClient.getInstance().trackServerLog(instance, logType);
        if (CollectionUtils.isEmpty(logContent)) {
            logContent = new ArrayList<>();
        }
        TrackServerLogResponse response = new TrackServerLogResponse();
        response.setSegmentLogs(logContent);

        String errorTips = "";
        // 1. 45格背包模组报错 786556008 playerlightningtarget
        // 3. 磁盘写满 There is not enough available hard drive space to reliably save worlds
        // 5. token错误 E_INVALID_TOKEN
        for (String log : logContent) {
            // 1.
            if (log.contains("playerlightningtarget")) {
                errorTips = "检测到崩溃的原因是由于开了45格背包【45 Inventory Slots】模组，该模组有bug, 遇到雷电法术攻击必定崩档, 请取消订阅该模组避免再次崩档问题。";
                response.setErrorTips(errorTips);
                return response;
            }
            // 3.
            if (log.contains("There is not enough available hard drive space to reliably save worlds")) {
                errorTips = "检测到饥荒产生大量错误内存dump文件, 导致硬盘空间不足, 你可联系客服进行处理.";
                response.setErrorTips(errorTips);
                return response;
            }
            // 5.
            if (log.contains("E_INVALID_TOKEN")) {
                errorTips = "检测到cluster_token错误, 你是自己上传的存档？里面放的cluster_token不正确，请你自己核查下。";
                response.setErrorTips(errorTips);
                return response;
            }
        }
        List<ModOverride> mods = DstClient.getInstance().getCurrentModOverrides(instance);
        List<String> overrideModIds = mods.stream().map(e -> e.getModId().replace("workshop-", "")).collect(Collectors.toList());
        // 2. 神话角色未开神话主题  角色 1699194522  主题 1991746508
        if (overrideModIds.contains("1699194522") && !overrideModIds.contains("1991746508")) {
            errorTips = "云服启动报错，因为你订阅了【神话角色】模组 但是并没订阅【神话主题】模组，神话角色依赖于神话主题模组才能使用。\n请自己到【模组管理】页面检查。";
            response.setErrorTips(errorTips);
            return response;
        }

        // 4. 提示具体报错mod id
        Set<Long> modIds = new HashSet<>();
        for (String logSegment : logContent) {
            Pattern pattern = Pattern.compile("workshop-(\\d+)");
            Matcher matcher = pattern.matcher(logSegment);
            try {
                while (matcher.find()) {
                    String group = matcher.group(1);
                    modIds.add(Long.parseLong(group));
                }
            } catch (Exception exception) {
                log.error("正则匹配报错模组ID失败.");
            }
        }
        if (modIds.size() != 0) {
            List<Long> modIdList = new ArrayList<>(modIds);
            List<DstModsDO> mbtDstModEntities = dstModDao.selectList(new LambdaQueryWrapperX<DstModsDO>().in(DstModsDO::getModId, modIdList));
            Map<Long, String> modIdMapTitle = mbtDstModEntities.stream().collect(Collectors.toMap(e -> e.getModId(), e -> e.getModTitle()));
            StringBuilder modIdJoin = new StringBuilder();
            for (Long modId : modIdList) {
                String modTitle = modIdMapTitle.get(modId);
                if (StringUtils.isEmpty(modTitle)) {
                    modIdJoin.append(modId).append(", ");
                } else {
                    modIdJoin.append(modId).append("(").append(modTitle).append(")").append(", ");
                }
            }
//            String modIdJoin = org.apache.commons.lang3.StringUtils.join(modIds, ", ");
            errorTips = "检测到模组报错, 可能是以下模组id中的一个或者多个导致: " + modIdJoin + "\n请自己到【模组管理】页面排查。友情提示：不要订阅低于5星或者4星评分的模组。";
            response.setErrorTips(errorTips);
            return response;
        }
        return response;
    }

    @Override
    public List<SteamModInfo> searchMod(String searchContent) {
        String modId = null;
        List<DstModsDO> mbtDstModEntities = new ArrayList<>();
        List<SteamModInfo> steamModInfos = new ArrayList<>();
        if (!StringUtils.hasText(searchContent)) {
            return steamModInfos;
        }
        if (searchContent.length() < 8 || !isNumber(searchContent)) {
            if (searchContent.contains("steamcommunity.com/sharedfiles")) {
                Pattern pattern = Pattern.compile("(\\d+)");
                Matcher matcher = pattern.matcher(searchContent);
                if (matcher.find()) {
                    modId = matcher.group(1);
                }
                if (modId == null) {
                    return steamModInfos;
                }
                mbtDstModEntities = dstModDao.selectList(new LambdaQueryWrapperX<DstModsDO>().eq(DstModsDO::getModId, Long.parseLong(modId)));
            } else {
                mbtDstModEntities = dstModDao.selectList(new LambdaQueryWrapperX<DstModsDO>().like(DstModsDO::getModTitle, searchContent));
                if (mbtDstModEntities.size() == 0) {
                    return steamModInfos;
                }
            }
        } else {
            modId = searchContent;
            mbtDstModEntities = dstModDao.selectList(new LambdaQueryWrapperX<DstModsDO>().eq(DstModsDO::getModId, Long.parseLong(modId)));
        }
        if (mbtDstModEntities != null && mbtDstModEntities.size() != 0) {
            for (DstModsDO mbtDstModEntity : mbtDstModEntities) {
                SteamModInfo steamModInfo = new SteamModInfo();
                steamModInfo.setModId(mbtDstModEntity.getModId().toString());
                steamModInfo.setIsServerMod(mbtDstModEntity.getIsServerMod());
                steamModInfo.setTags(mbtDstModEntity.getTags());
                steamModInfo.setTitle(mbtDstModEntity.getModTitle());
                steamModInfo.setFilesize(mbtDstModEntity.getFileSize());
                steamModInfo.setModPublishTime(mbtDstModEntity.getModPublishTime());
                steamModInfo.setModUpdateTime(mbtDstModEntity.getModUpdateTime());
                steamModInfo.setAuthor(mbtDstModEntity.getAuthor());
                steamModInfo.setVersion(mbtDstModEntity.getVersion());
                steamModInfo.setImage(mbtDstModEntity.getImage());
                steamModInfo.setStarUrl(mbtDstModEntity.getStarUrl());
                steamModInfo.setUrl(mbtDstModEntity.getUrl());
                steamModInfo.setStatus(mbtDstModEntity.getStatus());
                steamModInfos.add(steamModInfo);
            }
        }
        if (modId != null && (mbtDstModEntities == null || mbtDstModEntities.size() == 0)) {
            // 本地db未找到, 从steam拉取
            SteamModInfo modInfo = SteamUtil.getModInfo(modId);
            if (StringUtils.hasText(modInfo.getTitle())) {
                steamModInfos.add(modInfo);
                DstModsDO modEntity = new DstModsDO();
                modService.modInfoToEntity(modInfo, modEntity);
                if (modEntity.getModTitle() != null) {
                    dstModDao.insertOrUpdate(modEntity);
                }
            }
        }
        if (steamModInfos.size() > 100) {
            List<SteamModInfo> steamModInfosTemp = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                steamModInfosTemp.add(steamModInfos.get(i));
            }
            steamModInfos = steamModInfosTemp;
        }
        return steamModInfos;
    }

    private SteamModInfo entityToModInfo(DstModsDO modEntity, LocalModInfo modInfo) {
        modInfo.setModId(modEntity.getModId().toString());
        modInfo.setTitle(modEntity.getModTitle());
        modInfo.setUrl(modEntity.getUrl());
        modInfo.setIsServerMod(modEntity.getIsServerMod());
        modInfo.setTags(modEntity.getTags());
        modInfo.setImage(modEntity.getImage());
        modInfo.setStatus(modEntity.getStatus());
        modInfo.setAuthor(modEntity.getAuthor());
        modInfo.setVersion(modEntity.getVersion());
        modInfo.setFilesize(modEntity.getFileSize());
        modInfo.setModPublishTime(modEntity.getModPublishTime());
        modInfo.setModUpdateTime(modEntity.getModUpdateTime());
        modInfo.setStarUrl(modEntity.getStarUrl());
        return modInfo;
    }


    @Override
    public void deleteMod(String modId) {
        if (!isNumber(modId)) {
            return;
        }
        InstanceDO instance = getCurrentUserRentInstance();

        DstClient client = DstClient.getInstance();
        client.deleteModFileFromServer(instance, modId);
    }

    @Deprecated
    @Override
    public LocalModInfo subscribeMod(String modId) {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();

        List<LocalModInfo> localModInfos = client.checkDownloadAndGetConfig(instance, modId);
        if (CollectionUtils.isEmpty(localModInfos)) {
            throw new BusinessException("下载模组失败. ");
        }
        LocalModInfo localMod = localModInfos.get(0);
        localMod.setEnabled(true);// 主动订阅的模组自动启用.
        Map<String, Object> configuration_options = new HashMap<>();

        for (ModConfig modConfig : localMod.getModConfigs()) {
            if (!StringUtils.hasText(modConfig.getName())) {
                continue;
            }
            List<Option> options = modConfig.getOptions();
            if (options == null) {
                continue;
            }
            for (int i = 0; i < options.size(); i++) {
                Option option = options.get(i);
                if (option.getData().equals(modConfig.getDefaultOptionData())) {
                    modConfig.setOptionSelectedIndex(i);
                }
            }
            configuration_options.put(modConfig.getName(), modConfig.getDefaultOptionData());
        }
        localMod.setConfiguration_options(configuration_options);

        DstModsDO modEntity = dstModDao.selectOne(new LambdaQueryWrapperX<DstModsDO>().eq(DstModsDO::getModId, Long.parseLong(modId)));

        if (modEntity != null) {
            entityToModInfo(modEntity, localMod);
        } else {
            SteamModInfo steamModInfo = modService.updateDbMod(String.valueOf(modId));
            BeanUtils.copyProperties(steamModInfo, localMod);
        }
        return localMod;
    }

    @Override
    public void flushSteamAcfFile(String mod) {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();
        client.flushSteamAcfFile(instance, mod);
    }

    @Override
    public LocalModInfo subscribeModVersion2(String modId) {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();
        modId = modId.replace("workshop-", "");
        List<LocalModInfo> localModInfos = client.reDownloadAndGetConfig(instance, modId);
        if (CollectionUtils.isEmpty(localModInfos)) {
            // 第一次订阅失败后, 尝试清理acf文件再次
            deleteMod(modId);
            // 清理acf文件
            flushSteamAcfFile(modId);
            localModInfos = client.reDownloadAndGetConfig(instance, modId);
            if (CollectionUtils.isEmpty(localModInfos)) {
                throw new BusinessException("下载模组失败. ");
            }
        }
        LocalModInfo localMod = localModInfos.get(0);
        localMod.setEnabled(true);// 主动订阅的模组自动启用.
        Map<String, Object> configuration_options = new HashMap<>();

        for (ModConfig modConfig : localMod.getModConfigs()) {
            if (!StringUtils.hasText(modConfig.getName())) {
                continue;
            }
            List<Option> options = modConfig.getOptions();
            if (options == null) {
                continue;
            }
            for (int i = 0; i < options.size(); i++) {
                Option option = options.get(i);
                if (option.getData().equals(modConfig.getDefaultOptionData())) {
                    modConfig.setOptionSelectedIndex(i);
                }
            }
            configuration_options.put(modConfig.getName(), modConfig.getDefaultOptionData());
        }
        localMod.setConfiguration_options(configuration_options);

        DstModsDO modEntity = dstModDao.selectOne(new LambdaQueryWrapperX<DstModsDO>().eq(DstModsDO::getModId, modId));

        if (modEntity != null) {
            entityToModInfo(modEntity, localMod);
        } else {
            SteamModInfo steamModInfo = modService.updateDbMod(String.valueOf(modId));
            BeanUtils.copyProperties(steamModInfo, localMod);
        }
        return localMod;
    }

    @Override
    public void clearModCache() {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();
        client.clearModCache(instance);
    }

    @Override
    public List<LocalModInfo> getLocalMods() {
        // 1. 取回所有本地模组信息
        // 2. 获取存档模组配置信息
        // 3. 不存在的模组自动下载安装并返回模组配置信息
        // 4. 取出存档模组的现有配置
        // 5. 2和4两份配置合并, 标记存档中的配置信息.
        // 6. 取steam info, 返回前端

        StopWatch stopWatch = new StopWatch("获取模组");
        stopWatch.start();

        InstanceDO instance = getCurrentUserRentInstance();

        DstClient client = DstClient.getInstance();
        List<LocalModInfo> localMods = client.getLocalMods(instance);
        // modId 去重
        localMods = localMods.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(
                        Comparator.comparing(LocalModInfo::getModId))
                ), ArrayList::new)
        );

        List<String> localModIds = localMods.stream().map(e -> e.getModId().replaceAll("workshop-", "")).collect(Collectors.toList());// 1

        List<ModOverride> currentModOverrides = client.getCurrentModOverrides(instance);
        List<String> modIds = currentModOverrides.stream().map(e -> e.getModId().replaceAll("workshop-", "")).collect(Collectors.toList()); // 2
        Map<String, ModOverride> modIdMap = currentModOverrides.stream().collect(Collectors.toMap(e -> e.getModId().replaceAll("workshop-", ""), e -> e));

        // 3 列出modIds存在但是localModIds不存在的modId
        List<String> modsNeedDownloaded = modIds.stream().filter(e -> !localModIds.contains(e)).collect(Collectors.toList());
        if (modsNeedDownloaded.size() != 0) {
            String modIdStr = String.join(",", modsNeedDownloaded);
            List<LocalModInfo> newLocalMods = client.checkDownloadAndGetConfig(instance, modIdStr);
            localMods.addAll(newLocalMods);
        }
        // 4 取出存档模组的现有配置
        for (LocalModInfo localMod : localMods) {
            List<ModConfig> modConfigs = localMod.getModConfigs();
            // 5.1 将 modoverrides.lua中的已配置项反填到 LocalModInfo.configuration_options 中
            for (String modId : modIdMap.keySet()) {
                if (localMod.getModId().equals(modId)) {
                    localMod.setEnabled(modIdMap.get(modId).getEnabled());
                    Map<String, Object> configuration_options = modIdMap.get(modId).getConfiguration_options();
                    localMod.setConfiguration_options(configuration_options);
                    for (ModConfig modConfig : modConfigs) {
                        if (!StringUtils.hasText(modConfig.getName())) {
                            continue;
                        }
                        Object data = configuration_options.get(modConfig.getName());
                        List<Option> options = modConfig.getOptions();
                        if (options == null) {
                            continue;
                        }
                        for (int i = 0; i < options.size(); i++) {
                            Option option = options.get(i);
                            try {
                                if (option.getData().equals(data)) {
                                    modConfig.setOptionSelectedIndex(i);
                                }
                            } catch (Exception exception) {
                                /**
                                 *
                                 2022-10-31 17:56:24.453 [] ERROR 104233 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:516 modConfig:{"default":"glommerwings","hover":"Manufacture Of Material_1","label":"Material_1","name":"VKSR_S1","optionSelectedIndex":0,"options":[{"data":"glommerwings","description":"格洛姆的翅膀"},{"data":"tentaclespots","description":"触手怪的皮"},{"data":"walrus_tusk","description":"海象牙"},{"data":"nightmarefuel","description":"噩梦燃料"},{"data":"livinglog","description":"活木"},{"data":"horn","description":"毛牛角"},{"data":"deerclops_eyeball","description":"巨鹿的眼球"},{"data":"goose_feather","description":"鹿鸭的羽毛"},{"data":"bearger_fur","description":"熊怪的毛皮"},{"data":"gears","description":"齿轮"},{}]}
                                 2022-10-31 17:56:24.453 [] ERROR 104233 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:517 option:{}
                                 2022-10-31 17:56:24.454 [] ERROR 104234 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:518 Cannot invoke "Object.equals(Object)" because the return value of "cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo$ModConfig$Option.getData()" is null
                                 2022-10-31 17:56:24.454 [] ERROR 104234 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:516 modConfig:{"default":"tentaclespots","hover":"Manufacture Of Material_2","label":"Material_2","name":"VKSR_S2","optionSelectedIndex":1,"options":[{"data":"glommerwings","description":"格洛姆的翅膀"},{"data":"tentaclespots","description":"触手怪的皮"},{"data":"walrus_tusk","description":"海象牙"},{"data":"nightmarefuel","description":"噩梦燃料"},{"data":"livinglog","description":"活木"},{"data":"horn","description":"毛牛角"},{"data":"deerclops_eyeball","description":"巨鹿的眼球"},{"data":"goose_feather","description":"鹿鸭的羽毛"},{"data":"bearger_fur","description":"熊怪的毛皮"},{"data":"gears","description":"齿轮"},{}]}
                                 2022-10-31 17:56:24.454 [] ERROR 104234 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:517 option:{}
                                 2022-10-31 17:56:24.455 [] ERROR 104235 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:518 Cannot invoke "Object.equals(Object)" because the return value of "cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo$ModConfig$Option.getData()" is null
                                 2022-10-31 17:56:24.455 [] ERROR 104235 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:516 modConfig:{"default":"walrus_tusk","hover":"Manufacture Of Material_3","label":"Material_3","name":"VKSR_S3","optionSelectedIndex":2,"options":[{"data":"glommerwings","description":"格洛姆的翅膀"},{"data":"tentaclespots","description":"触手怪的皮"},{"data":"walrus_tusk","description":"海象牙"},{"data":"nightmarefuel","description":"噩梦燃料"},{"data":"livinglog","description":"活木"},{"data":"horn","description":"毛牛角"},{"data":"deerclops_eyeball","description":"巨鹿的眼球"},{"data":"goose_feather","description":"鹿鸭的羽毛"},{"data":"bearger_fur","description":"熊怪的毛皮"},{"data":"gears","description":"齿轮"},{}]}
                                 2022-10-31 17:56:24.455 [] ERROR 104235 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:517 option:{}
                                 */
                                log.error("modId:{}, modConfig:{}", modId, JSON.toJSONString(modConfig));
                                log.error("modId:{}, option:{}", modId, JSON.toJSONString(option));
                                log.error(exception.getMessage());
                            }
                        }
                    }
                    break;
                }
            }
            // 5.2 根据模组默认的配置 填充到 LocalModInfo.configuration_options 中
            if (localMod.getEnabled() == null) {
                localMod.setEnabled(false);
                Map<String, Object> configuration_options = new HashMap<>();

                for (ModConfig modConfig : modConfigs) {
                    if (!StringUtils.hasText(modConfig.getName())) {
                        continue;
                    }
                    List<Option> options = modConfig.getOptions();
                    if (options == null) {
                        continue;
                    }
                    for (int i = 0; i < options.size(); i++) {
                        Option option = options.get(i);
                        try {
                            if (option.getData().equals(modConfig.getDefaultOptionData())) {
                                modConfig.setOptionSelectedIndex(i);
                            }
                        } catch (Exception exception) {
                            /**
                             *
                             2022-10-31 17:56:24.453 [] ERROR 104233 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:516 modConfig:{"default":"glommerwings","hover":"Manufacture Of Material_1","label":"Material_1","name":"VKSR_S1","optionSelectedIndex":0,"options":[{"data":"glommerwings","description":"格洛姆的翅膀"},{"data":"tentaclespots","description":"触手怪的皮"},{"data":"walrus_tusk","description":"海象牙"},{"data":"nightmarefuel","description":"噩梦燃料"},{"data":"livinglog","description":"活木"},{"data":"horn","description":"毛牛角"},{"data":"deerclops_eyeball","description":"巨鹿的眼球"},{"data":"goose_feather","description":"鹿鸭的羽毛"},{"data":"bearger_fur","description":"熊怪的毛皮"},{"data":"gears","description":"齿轮"},{}]}
                             2022-10-31 17:56:24.453 [] ERROR 104233 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:517 option:{}
                             2022-10-31 17:56:24.454 [] ERROR 104234 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:518 Cannot invoke "Object.equals(Object)" because the return value of "cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo$ModConfig$Option.getData()" is null
                             2022-10-31 17:56:24.454 [] ERROR 104234 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:516 modConfig:{"default":"tentaclespots","hover":"Manufacture Of Material_2","label":"Material_2","name":"VKSR_S2","optionSelectedIndex":1,"options":[{"data":"glommerwings","description":"格洛姆的翅膀"},{"data":"tentaclespots","description":"触手怪的皮"},{"data":"walrus_tusk","description":"海象牙"},{"data":"nightmarefuel","description":"噩梦燃料"},{"data":"livinglog","description":"活木"},{"data":"horn","description":"毛牛角"},{"data":"deerclops_eyeball","description":"巨鹿的眼球"},{"data":"goose_feather","description":"鹿鸭的羽毛"},{"data":"bearger_fur","description":"熊怪的毛皮"},{"data":"gears","description":"齿轮"},{}]}
                             2022-10-31 17:56:24.454 [] ERROR 104234 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:517 option:{}
                             2022-10-31 17:56:24.455 [] ERROR 104235 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:518 Cannot invoke "Object.equals(Object)" because the return value of "cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo$ModConfig$Option.getData()" is null
                             2022-10-31 17:56:24.455 [] ERROR 104235 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:516 modConfig:{"default":"walrus_tusk","hover":"Manufacture Of Material_3","label":"Material_3","name":"VKSR_S3","optionSelectedIndex":2,"options":[{"data":"glommerwings","description":"格洛姆的翅膀"},{"data":"tentaclespots","description":"触手怪的皮"},{"data":"walrus_tusk","description":"海象牙"},{"data":"nightmarefuel","description":"噩梦燃料"},{"data":"livinglog","description":"活木"},{"data":"horn","description":"毛牛角"},{"data":"deerclops_eyeball","description":"巨鹿的眼球"},{"data":"goose_feather","description":"鹿鸭的羽毛"},{"data":"bearger_fur","description":"熊怪的毛皮"},{"data":"gears","description":"齿轮"},{}]}
                             2022-10-31 17:56:24.455 [] ERROR 104235 http-nio-18100-exec-2 com.dooyo.service.impl.DstServiceImpl:getLocalMods:517 option:{}
                             */
                            log.error("modConfig:{}", JSON.toJSONString(modConfig));
                            log.error(" option:{}", JSON.toJSONString(option));
                            log.error(exception.getMessage());
                        }

                    }
                    configuration_options.put(modConfig.getName(), modConfig.getDefaultOptionData());
                }
                localMod.setConfiguration_options(configuration_options);
            }

        }
        // 6 取steam info, 合并显示模组更多的属性信息.
        List<Long> localModIdsLong = localMods.stream().map(e -> Long.parseLong(e.getModId().replaceAll("workshop-", ""))).collect(Collectors.toList());// 1
        List<DstModsDO> mbtDstModEntities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(localModIdsLong)) {
            mbtDstModEntities = dstModDao.selectList(new LambdaQueryWrapperX<DstModsDO>().in(DstModsDO::getModId, localModIdsLong));
        }

        List<Long> modsInDb = mbtDstModEntities.stream().map(e -> e.getModId()).collect(Collectors.toList());

        List<Long> modsInfoNeedFetchedFromSteam = localModIdsLong.stream().filter(e -> !modsInDb.contains(e)).collect(Collectors.toList());
        if (modsInfoNeedFetchedFromSteam.size() != 0) {
            List<SteamModInfo> steamModInfos = new ArrayList<>();
            for (Long modId : modsInfoNeedFetchedFromSteam) {
                SteamModInfo steamModInfo = modService.updateDbMod(String.valueOf(modId));
                steamModInfos.add(steamModInfo);
            }
            for (LocalModInfo localMod : localMods) {
                for (SteamModInfo steamModInfo : steamModInfos) {
                    if (localMod.getModId().equals(steamModInfo.getModId())) {
                        BeanUtils.copyProperties(steamModInfo, localMod);
                    }
                }
            }
        }
        for (DstModsDO mbtDstModEntity : mbtDstModEntities) {
            for (LocalModInfo localMod : localMods) {
                if (mbtDstModEntity.getModId().toString().equals(localMod.getModId())) {
                    entityToModInfo(mbtDstModEntity, localMod);
                }
            }
        }

        for (LocalModInfo localMod : localMods) {
            if (localMod.getConfiguration_options() == null) {
                Map<String, Object> configuration_options = new HashMap<>();
                for (ModConfig modConfig : localMod.getModConfigs()) {
                    configuration_options.put(modConfig.getName(), modConfig.getDefaultOptionData());
                }
                localMod.setConfiguration_options(configuration_options);
            }
        }

        stopWatch.stop();
        log.info("{}耗时:{}", stopWatch.getId(), stopWatch.getTotalTimeMillis());
        return localMods;
    }

    private boolean isNumber(String str) {
        return str.matches("[0-9]+");
    }

    @Override
    public void overrideArchive(Integer day) {
        zipArchive();
        String command = String.format("c_rollback(%d)", day);
        DstClient.getInstance().executeCommand(getCurrentUserRentInstance(), command);
    }

    @Override
    public void saveCurrentWorld() {
        DstClient.getInstance().executeCommand(getCurrentUserRentInstance(), "c_save()");
    }

    @Override
    public void rePickCharacter(String number) {
        DstClient.getInstance().executeCommand(getCurrentUserRentInstance(), String.format("c_despawn(AllPlayers[%s])", number));
    }

    @Override
    public void managePlayer(PlayManageParam param) {
        DstClient instance = DstClient.getInstance();

        switch (param.getType()) {
            case kick -> instance.executeCommand(getCurrentUserRentInstance(), String.format("TheNet:Kick('%s')", param.getKleiId()));
            case despawn -> instance.executeCommand(getCurrentUserRentInstance(), String.format("c_despawn(UserToPlayer('%s'))", param.getKleiId()));// c_despawn(UserToPlayer('KU_SFlxVROJ'))
            case killToDeath -> instance.executeCommand(getCurrentUserRentInstance(), String.format("UserToPlayer('%s'):PushEvent('death')", param.getKleiId()));// UserToPlayer('KU_SFlxVROJ'):PushEvent("death")
            case respawnfromghost ->
                    instance.executeCommand(getCurrentUserRentInstance(), String.format("UserToPlayer('%s'):PushEvent('respawnfromghost')", param.getKleiId()));// UserToPlayer('KU_SFlxVROJ'):PushEvent("respawnfromghost")
        }


    }

    @Override
    public void announcement(String content) {
        DstClient instance = DstClient.getInstance();
        instance.executeCommand(getCurrentUserRentInstance(), String.format("c_announce(\"%s\")", content));
    }

    @Override
    public void restartClusterWithoutSave() {
        DstClient.getInstance().executeCommand(getCurrentUserRentInstance(), "c_reset()");
    }

    @Override
    public void reGenerateClusterWithoutSave() {
        DstClient.getInstance().executeCommand(getCurrentUserRentInstance(), "c_regenerateworld()");
    }

    // 强制结束该机器上的screen进程.
    public void forceCloseScreenProcess(InstanceDO instance) {
        String cmd = "screen -dr master_screen -X -S quit";
        String cmd2 = "screen -dr caves_screen -X -S quit";
        Shell shell = new Shell(instance.getIp(), "root", instance.getSshPort(), instance.getSshPassword());
        shell.execCommand(cmd);
        shell.execCommand(cmd2);
    }

    @Override
    public void forceReGenerateWorld() {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();

        WorldStatus worldStatus = client.queryWorldsStatus(instance);
        if (worldStatus.getIsCavesExist() || worldStatus.getIsMasterExist()) {
            client.stopMaster(instance);
            client.stopCaves(instance);
//            long startTime = new Date().getTime();//ms
//            while (true) {
//                try {
//                    Thread.sleep(1500);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                worldStatus = client.queryWorldsStatus(instance);
//                if (!worldStatus.getIsCavesExist() && !worldStatus.getIsMasterExist()) {
//                    break;
//                }
//                long endTime = new Date().getTime();//ms
//                if ((endTime - startTime) / 1000 > 10) {// 超过10秒 自动继续
//                    client.stopMaster(instance);
//                    client.stopCaves(instance);
//                    break;
//                }
//            }
        }
        forceCloseScreenProcess(instance);
        zipArchive();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.forceReGenerateWorld(instance);

//        client.startMaster(ip);
//        client.startCaves(ip);
    }

    @Override
    public void forceReGenerateWorld2() {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();

        client.stopMaster(instance);
        client.stopCaves(instance);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.zipArchive(instance, "");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.forceReGenerateWorld2(instance);
        // 重置世界功能, 不再自动重启. 该功能只会先停止云服进程, 使用默认存档覆盖(保留用户现有存档的世界配置和模组配置和房间配置), 完事后需要用户自己重启服务器.
     /*   try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.startMaster(ip);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.startCaves(ip);*/
    }

    @Override
    public List<ModOverride> getCurrentMods() {
        InstanceDO instance = getCurrentUserRentInstance();

        DstClient client = DstClient.getInstance();
        List<ModOverride> currentModOverrides = client.getCurrentModOverrides(instance);
        return currentModOverrides;
    }

    /**
     * 字符串是否包含中文
     *
     * @param str 待校验字符串
     * @return true 包含中文字符 false 不包含中文字符
     */
    private boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    @Override
    public void saveModOverride(List<ModOverride> modOverrides) {
        InstanceDO instance = getCurrentUserRentInstance();

        DstClient client = DstClient.getInstance();

        String space = "\s\s";
        String lineSeparator = "\n";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("return {").append(lineSeparator);

        int modSize = modOverrides.size();
        int modIndex = 1;
        for (ModOverride modOverride : modOverrides) {
            stringBuilder.append(space).append(String.format("[\"%s\"]={", modOverride.getModId())).append(lineSeparator);
            Boolean enabled = modOverride.getEnabled();
            Map<String, Object> options = modOverride.getConfiguration_options();
            enabled = enabled != null;
            stringBuilder.append(space).append(space).append("enabled=").append(enabled).append(",").append(lineSeparator);
            if (options == null || options.size() == 0) {
                stringBuilder.append(space).append(space).append("configuration_options={}").append(lineSeparator);
            } else {
                stringBuilder.append(space).append(space).append("configuration_options={").append(lineSeparator);
                int optionSize = options.size();
                int optionIndex = 1;
                // 这里遍历configuration_options, 大多数modoverrides.lua保存出问题都需要从这里处理.
                for (String key : modOverride.getConfiguration_options().keySet()) {
                    if (key.contains(" ")) { // 有的模组作者会拿option当作小标题... 这里针对带空格的选项做移除.
                        continue;
                    }
                    if (!StringUtils.hasText(key.trim())) {
                        continue;// 有的模组作者会拿option当作小标题... 这里针对带空格的选项做移除.
                    }
                    if (isContainChinese(key)) {
                        continue;
                    }
                    Object value = options.get(key);
//                    if (value instanceof String) {
//                        value = "\"" + value + "\"";
//                    }
                    if (key.contains("-") || key.contains(":")) {
                        stringBuilder.append(space).append(space).append(space).append("[\"" + key + "\"]").append("=").append(valueParser(value));
                    } else {
                        stringBuilder.append(space).append(space).append(space).append(key).append("=").append(valueParser(value));
                    }
                    if (optionIndex != optionSize) {
                        stringBuilder.append(",").append(lineSeparator);
                    } else {
                        stringBuilder.append(lineSeparator);
                    }
                    optionIndex++;
                }
                stringBuilder.append(space).append(space).append("}").append(lineSeparator);
            }
            stringBuilder.append(space).append("}");
            if (modIndex != modSize) {
                stringBuilder.append(",").append(lineSeparator);
            } else {
                stringBuilder.append(lineSeparator);
            }
            modIndex++;
        }
        stringBuilder.append("}");

        client.saveModOverride(instance, stringBuilder.toString());
    }

    // 递归解析value, 因为发现有的模组, kv的value不仅仅是基本类型的值, 也有可能是一个table(lua)
    @Deprecated
    public String valueParser(Object value) {
        String space = "\s\s";

        if (value instanceof String) {
            value = "\"" + value + "\"";
        } else if (
                value instanceof Boolean || value instanceof Integer || value instanceof Double || value instanceof Float || value instanceof Long
                        || value instanceof BigDecimal
        ) {
            // do nothing
            value = value;
        } else if (value instanceof JSONObject) {
//            StringBuilder stringBuilder = new StringBuilder();
            value = recursiveValueParser((JSONObject) value, "");
//            log.info(value.toString());
        } else {
            value = "{}";
        }
        return value.toString();
    }

    public String objectParser(Object value, String space) {
//        String space = "\s\s";

        if (value instanceof String) {
            value = "\"" + value + "\"";
        } else if (
                value instanceof Boolean || value instanceof Integer || value instanceof Double || value instanceof Float || value instanceof Long
                        || value instanceof BigDecimal
        ) {
            // do nothing
            value = value;
        } else if (value instanceof JSONArray) {
            StringBuilder stringBuilderArray = new StringBuilder();
            stringBuilderArray.append("{");
            for (int i = 0; i < ((JSONArray) value).size(); i++) {
//                    JSONObject jsonObject = ((JSONArray) value).getJSONObject(i);
                Object obj = ((JSONArray) value).get(i);
//                    StringBuilder stringBuilderObj = new StringBuilder();
                String s = objectParser(obj, space + "\s\s");
                stringBuilderArray.append(s);
                if (i < ((JSONArray) value).size() - 1) {
                    stringBuilderArray.append(",");
                }
                stringBuilderArray.append("\n").append(space);
            }
            stringBuilderArray.append("}");
            value = stringBuilderArray.toString();
//            log.info(value.toString());
        } else if (value instanceof JSONObject) {
//            StringBuilder stringBuilder = new StringBuilder();
            value = recursiveValueParser((JSONObject) value, space);
//            log.info(value.toString());
        } else {
            value = "{}";
        }
        return value.toString();
    }

    public String recursiveValueParser(JSONObject object, String space) {
//        space = space + "\s\s";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\s\s").append("{");
        int size = 0;
        for (String key : object.keySet()) {
            size++;
            stringBuilder.append("\n").append(space + "\s\s").append("[\"" + key + "\"]=");
            Object value = object.get(key);
            stringBuilder.append(objectParser(value, space + "\s\s"));
            if (size != object.keySet().size()) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("\n").append(space).append("}");
        return stringBuilder.toString();
    }


    @Override
    public void saveModOverrideVersion2(List<ModOverride> modOverrides) {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();

        String space = "\s\s";
        String lineSeparator = "\n";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("return {").append(lineSeparator);

        int modSize = modOverrides.size();
        int modIndex = 1;
        for (ModOverride modOverride : modOverrides) {
            stringBuilder.append(space).append(String.format("[\"%s\"]={", modOverride.getModId())).append(lineSeparator);
            Boolean enabled = modOverride.getEnabled();
            Map<String, Object> options = modOverride.getConfiguration_options();
            enabled = enabled != null;
            stringBuilder.append(space).append(space).append("[\"enabled\"]=").append(enabled).append(",").append(lineSeparator);
            if (options == null || options.size() == 0) {
                stringBuilder.append(space).append(space).append("[\"configuration_options\"]={}").append(lineSeparator);
            } else {
                stringBuilder.append(space).append(space).append("[\"configuration_options\"]={").append(lineSeparator);
                int optionSize = options.size();
                int optionIndex = 1;
                // 这里遍历configuration_options, 大多数modoverrides.lua保存出问题都需要从这里处理.
                for (String key : modOverride.getConfiguration_options().keySet()) {
                    if (!StringUtils.hasText(key.trim())) {
                        continue;// 有的模组作者会拿option当作小标题... 这里针对带空格的选项做移除.
                    }
//                    if (key.contains(" ")) { // 有的模组作者会拿option当作小标题... 这里针对带空格的选项做移除.
//                        continue;
//                    }
//                    if (isContainChinese(key)) {
//                        continue;
//                    }
                    Object value = options.get(key);
//                    if (value instanceof String) {
//                        value = "\"" + value + "\"";
//                    }
                    stringBuilder.append(space).append(space).append(space).append("[\"" + key + "\"]").append("=").append(objectParser(value, space + space + space));

                    if (optionIndex != optionSize) {
                        stringBuilder.append(",").append(lineSeparator);
                    } else {
                        stringBuilder.append(lineSeparator);
                    }
                    optionIndex++;
                }
                stringBuilder.append(space).append(space).append("}").append(lineSeparator);
            }
            stringBuilder.append(space).append("}");
            if (modIndex != modSize) {
                stringBuilder.append(",").append(lineSeparator);
            } else {
                stringBuilder.append(lineSeparator);
            }
            modIndex++;
        }
        stringBuilder.append("}");

        client.saveModOverride(instance, stringBuilder.toString());
    }


    public void updateDbTimer() {
        // 最大5个线程，任务队列20个，拒绝策略为`主线程运行`.
        ExecutorService steamExecutor = new ThreadPoolExecutor(10, 20, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        int pageNum = 1;
        int pageSize = 1000;
        while (true) {
//            PageUtils.startPage(pageNum, pageSize);
            PageParam pageParam = new PageParam();
            pageParam.setPageNo(pageNum);
            pageParam.setPageSize(pageSize);

            List<DstModsDO> mbtDstModEntities = dstModDao.selectPage(pageParam, new LambdaQueryWrapperX<>()).getList();
            if (mbtDstModEntities == null || mbtDstModEntities.size() == 0) {
                break;
            }

            CountDownLatch countDownLatch = new CountDownLatch(mbtDstModEntities.size());
            for (DstModsDO modEntity : mbtDstModEntities) {
                steamExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String modId = modEntity.getModId().toString();
                            modService.updateDbMod(modId);
                        } catch (Exception ignore) {
                        } finally {
                            countDownLatch.countDown();
                        }
                    }
                });
            }
            try {
                countDownLatch.await(20, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                log.error("定时拉取mod信息超时10min");
                throw new RuntimeException(e);
            } finally {
                pageNum++;
            }
        }
    }


    @Override
    public void saveLevelOverride(WorldLevelOverrideParam param) {
        InstanceDO instance = getCurrentUserRentInstance();

        DstClient client = DstClient.getInstance();

        WorldOverride worldOverride = param.getWorldOverride();
        Map<String, String> map = new HashMap<>();
        map.put("raw", worldOverride.getRaw());
        map.put("override", worldOverride.getOverride());
        client.saveLevelOverride(instance, map, param.getWorldType());
    }

    @Override
    public void saveWorldOptionWithMode(WorldType worldType, GameMode mode) {
        List<WorldOptionWithMode> worldOptionWithMode = getWorldOptionWithMode(worldType, mode);
        Map<String, WorldOptionWithMode> keyMapOption = worldOptionWithMode.stream().collect(Collectors.toMap(e -> e.getKey(), e -> e));
        LevelConfig levelOverride = getLevelOverride(worldType);
        Map<String, Object> map = new HashMap<>();
        // 1 世界选项
        Map<String, List<LevelConfigItem>> optionResourceList = levelOverride.getOptionResourceList();
        for (String resourceType : optionResourceList.keySet()) {
            List<LevelConfigItem> levelConfigItems = optionResourceList.get(resourceType);
            for (LevelConfigItem levelConfigItem : levelConfigItems) {
                WorldOptionWithMode optionWithMode = keyMapOption.get(levelConfigItem.getKey());
                if (optionWithMode == null) {
                    map.put(levelConfigItem.getKey(), levelConfigItem.getData());
                } else {
                    map.put(levelConfigItem.getKey(), optionWithMode.getOptionKey());
                }
            }
        }
        // 2 世界生成
        Map<String, List<LevelConfigItem>> generateResourceList = levelOverride.getGenerateResourceList();
        for (String resourceType : generateResourceList.keySet()) {
            List<LevelConfigItem> levelConfigItems = generateResourceList.get(resourceType);
            for (LevelConfigItem levelConfigItem : levelConfigItems) {
                WorldOptionWithMode optionWithMode = keyMapOption.get(levelConfigItem.getKey());
                if (optionWithMode == null) {
                    map.put(levelConfigItem.getKey(), levelConfigItem.getData());
                } else {
                    map.put(levelConfigItem.getKey(), optionWithMode.getOptionKey());
                }
            }
        }
        // 3 默认配置项
        List<LevelConfigItem> defaultOptions = levelOverride.getDefaultOption();
        for (LevelConfigItem levelConfigItem : defaultOptions) {
            map.put(levelConfigItem.getKey(), levelConfigItem.getData());
        }
        String override = JSON.toJSONString(map);

        WorldLevelOverrideParam param = new WorldLevelOverrideParam();
        param.setWorldType(worldType);
        WorldOverride worldOverride = new WorldOverride();
        worldOverride.setRaw(levelOverride.getRaw());
        worldOverride.setOverride(override);
        param.setWorldOverride(worldOverride);
        log.info(JSON.toJSONString(param));
        saveLevelOverride(param);

        String gameCode = mode.getCode();
        String gameCodeStr = mode.getName();
        if (mode == GameMode.recommend_performance) {
            gameCode = GameMode.endless.getCode();
            gameCodeStr = GameMode.endless.getName();
        }
        // 保存游戏模式
        ClusterConfig clusterConfig = getClusterConfig();
        clusterConfig.setGameMode(gameCode);
        clusterConfig.setGameModeStr(gameCodeStr);
        saveClusterConfig(clusterConfig);
    }

    @Override
    public List<WorldOptionWithMode> getWorldOptionWithMode(WorldType worldType, GameMode mode) {
        if (mode == null) {
            throw new BusinessException("请指定一个游戏模式.");
        }
        LambdaQueryWrapperX<DstWorldOptionDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(DstWorldOptionDO::getStatus, 1);
        if (worldType != null) {
            queryWrapperX.eq(DstWorldOptionDO::getWorld, worldType.name());
        }
        switch (mode) {
            case recommend_performance -> queryWrapperX.isNotNull(DstWorldOptionDO::getRecommendOption);
            case relaxed -> queryWrapperX.isNotNull(DstWorldOptionDO::getRelaxedOption);
            case endless -> queryWrapperX.isNotNull(DstWorldOptionDO::getEndlessOption);
            case survival -> queryWrapperX.isNotNull(DstWorldOptionDO::getSurvivalOption);
            case wilderness -> queryWrapperX.isNotNull(DstWorldOptionDO::getWildernessOption);
            case lightsout -> queryWrapperX.isNotNull(DstWorldOptionDO::getLightsoutOption);
        }

        List<DstWorldOptionDO> mbtDstWorldOptionEntities = worldOptionDao.selectList(queryWrapperX);
        
        List<WorldOptionWithMode> worldOptionWithModes = new ArrayList<>();
        for (DstWorldOptionDO mbtDstWorldOptionEntity : mbtDstWorldOptionEntities) {
            WorldOptionWithMode worldOptionWithMode = new WorldOptionWithMode();
            worldOptionWithMode.setKey(mbtDstWorldOptionEntity.getKeyName());
            worldOptionWithMode.setLabel(mbtDstWorldOptionEntity.getLabel());
            switch (mode) {
                case recommend_performance -> worldOptionWithMode.setOptionKey(mbtDstWorldOptionEntity.getRecommendOption());
                case relaxed -> worldOptionWithMode.setOptionKey(mbtDstWorldOptionEntity.getRelaxedOption());
                case endless -> worldOptionWithMode.setOptionKey(mbtDstWorldOptionEntity.getEndlessOption());
                case survival -> worldOptionWithMode.setOptionKey(mbtDstWorldOptionEntity.getSurvivalOption());
                case wilderness -> worldOptionWithMode.setOptionKey(mbtDstWorldOptionEntity.getWildernessOption());
                case lightsout -> worldOptionWithMode.setOptionKey(mbtDstWorldOptionEntity.getLightsoutOption());
            }
            String options = mbtDstWorldOptionEntity.getOptions();
            JSONArray objects = JSON.parseArray(options);
            for (Object object : objects) {
                if (worldOptionWithMode.getOptionKey().equals(((JSONObject) object).getString("key"))) {
                    worldOptionWithMode.setOptionLabel(((JSONObject) object).getString("label"));
                    break;
                }
            }
            if (worldOptionWithMode.getOptionKey().equals(mbtDstWorldOptionEntity.getDefaultOption())) {
                worldOptionWithMode.setSpecialMode(false);
            } else {
                worldOptionWithMode.setSpecialMode(true);
            }
            worldOptionWithModes.add(worldOptionWithMode);
        }
        return worldOptionWithModes;
    }

    @Override
    public LevelConfig getLevelOverride(WorldType worldType) {
        Long userId = HeaderHolder.getCurrentUserId();

        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();
        Map<String, String> map = client.readLevelOverride(instance, worldType);
        String raw = map.get("raw");
        LevelConfig levelConfig = new LevelConfig();
        levelConfig.setRaw(raw);
        
        LambdaQueryWrapper<DstWorldOptionDO> queryWrapper = new LambdaQueryWrapperX<DstWorldOptionDO>().orderByAsc(DstWorldOptionDO::getSort);
        assert userId != null;
        if (userId.equals(1L)) {
            queryWrapper.eq(DstWorldOptionDO::getWorld, worldType.name());
        } else {
            queryWrapper.eq(DstWorldOptionDO::getWorld, worldType.name()).eq(DstWorldOptionDO::getStatus, 1);
        }

        List<DstWorldOptionDO> mbtDstWorldOptionEntities = worldOptionDao.selectList(queryWrapper);

        Map<String, DstWorldOptionDO> keyMap = mbtDstWorldOptionEntities.stream().collect(Collectors.toMap(e -> e.getKeyName(), e -> e));

        String override = map.get("override");
        JSONObject jsonObject = JSON.parseObject(override);
        List<LevelConfigItem> items = new ArrayList<>();
        /*for (String key : keyMap.keySet()) {
            DstWorldOptionDO mbtDstWorldOptionEntity = keyMap.get(key);
            Object value = jsonObject.get(key);
            LevelConfigItem item = new LevelConfigItem();
            if (value == null) {
                item.setData(mbtDstWorldOptionEntity.getDefaultOption());
            } else {
                item.setData(value);
            }
            item.setKey(key);
            item.setOrder(mbtDstWorldOptionEntity.getSort());
            item.setIcon(mbtDstWorldOptionEntity.getIcon());
            item.setConfigType(mbtDstWorldOptionEntity.getConfigType());
            item.setResourceType(mbtDstWorldOptionEntity.getResourceType());
            item.setLabel(mbtDstWorldOptionEntity.getLabel());
            item.setDefaultOption(mbtDstWorldOptionEntity.getDefaultOption());
            String options = mbtDstWorldOptionEntity.getOptions();
            List<LevelOption> optionList = JSON.parseArray(options, LevelOption.class);
            item.setOptions(optionList);
            items.add(item);
        }*/
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            LevelConfigItem item = new LevelConfigItem();
            item.setKey(key);
            item.setData(value);
            item.setOrder(999);
            DstWorldOptionDO mbtDstWorldOptionEntity = keyMap.get(key);
            if (mbtDstWorldOptionEntity == null) {
                items.add(item);
                continue;
            }
            item.setOrder(mbtDstWorldOptionEntity.getSort());
            item.setIcon(mbtDstWorldOptionEntity.getIcon());
            item.setConfigType(mbtDstWorldOptionEntity.getConfigType());
            item.setResourceType(mbtDstWorldOptionEntity.getResourceType());
            item.setLabel(mbtDstWorldOptionEntity.getLabel());
            item.setGlobalConfig(mbtDstWorldOptionEntity.getGlobalConfig());
            item.setDefaultOption(mbtDstWorldOptionEntity.getDefaultOption());
            String options = mbtDstWorldOptionEntity.getOptions();
            List<LevelOption> optionList = JSON.parseArray(options, LevelOption.class);
            item.setOptions(optionList);
            items.add(item);
        }
        // 有可能有一些参数, 并没写进leveloverride.lua文件中, 但是在ui里面是支持配置的, 把这些补上, 全设置成默认的. 一般是因为科雷更新, 增加了一些选项, 而老存档是没有这些选项的.
        Map<String, LevelConfigItem> keyMapOption = items.stream().collect(Collectors.toMap(e -> e.getKey(), e -> e));
        List<LevelConfigItem> itemsNeedAdd = new ArrayList<>(); // 存放需要补进去的配置项
        for (String keyInDb : keyMap.keySet()) {
            LevelConfigItem item = keyMapOption.get(keyInDb);
            if (item != null) {
                continue;
            }
            item = new LevelConfigItem();
            DstWorldOptionDO mbtDstWorldOptionEntity = keyMap.get(keyInDb);
            item.setData(mbtDstWorldOptionEntity.getDefaultOption());
            item.setKey(mbtDstWorldOptionEntity.getKeyName());
            item.setOrder(mbtDstWorldOptionEntity.getSort());
            item.setIcon(mbtDstWorldOptionEntity.getIcon());
            item.setConfigType(mbtDstWorldOptionEntity.getConfigType());
            item.setResourceType(mbtDstWorldOptionEntity.getResourceType());
            item.setGlobalConfig(mbtDstWorldOptionEntity.getGlobalConfig());
            item.setLabel(mbtDstWorldOptionEntity.getLabel());
            item.setDefaultOption(mbtDstWorldOptionEntity.getDefaultOption());
            String options = mbtDstWorldOptionEntity.getOptions();
            List<LevelOption> optionList = JSON.parseArray(options, LevelOption.class);
            item.setOptions(optionList);
            itemsNeedAdd.add(item);
        }
        items.addAll(itemsNeedAdd);

        Map<String, List<LevelConfigItem>> optionResourceList = new LinkedHashMap<>();// 世界选项
        Map<String, List<LevelConfigItem>> generateResourceList = new LinkedHashMap<>();// 世界生成
        List<LevelConfigItem> defaultOption = new ArrayList<>(); // 默认配置，前端无需展示. 但是需要取出值塞到保存的json中. 这里的默认配置项，指的是leveloverride.lua文件里面，那些不需要、也不允许用户自行更改的参数. 大概好像有5-10个这样的参数吧。
        List<LevelConfigItem> newItems = items.stream().sorted(Comparator.comparing(LevelConfigItem::getOrder)).collect(Collectors.toList());
        for (LevelConfigItem item : newItems) {
            if (item.getConfigType() == null) {
                defaultOption.add(item);
            } else if (item.getConfigType().equals("世界选项")) {
                List<LevelConfigItem> levelConfigItems = optionResourceList.get(item.getResourceType());
                if (levelConfigItems == null) {
                    levelConfigItems = new ArrayList<>();
                }
                levelConfigItems.add(item);
                optionResourceList.put(item.getResourceType(), levelConfigItems);
            } else if (item.getConfigType().equals("世界生成")) {
                List<LevelConfigItem> levelConfigItems = generateResourceList.get(item.getResourceType());
                if (levelConfigItems == null) {
                    levelConfigItems = new ArrayList<>();
                }
                levelConfigItems.add(item);
                generateResourceList.put(item.getResourceType(), levelConfigItems);
            }
        }
        levelConfig.setDefaultOption(defaultOption); // 这里的默认配置项，指的是leveloverride.lua文件里面，那些不需要、也不允许用户自行更改的参数. 大概好像有5-10个这样的参数吧。
        levelConfig.setOptionResourceList(optionResourceList);
        levelConfig.setGenerateResourceList(generateResourceList);

        return levelConfig;
    }

    @Override
    public ProcessInfo getCurrentProcessInfo() {
        InstanceDO instance = getCurrentUserRentInstance();
        String resInfo = DstClient.getInstance().getProcessInfo(instance);
        ProcessInfo processInfo = JSON.parseObject(resInfo, ProcessInfo.class);
        Assert.notNull(processInfo, "解析进程信息失败.");
        ProcessItem master = processInfo.getMaster();
        master.setIsRunning(true);
        if (master.getPid() == 0) {
            master.setIsRunning(false);
        }
        ProcessItem caves = processInfo.getCaves();
        caves.setIsRunning(true);
        if (caves.getPid() == 0) {
            caves.setIsRunning(false);
        }
        processInfo.setIsRunning(true);
        if (!master.getIsRunning() && !caves.getIsRunning()) {
            processInfo.setIsRunning(false);
        }
        return processInfo;
    }

    @Override
    public List<ArchiveFileInfo> getArchiveList() {
        InstanceDO instance = getCurrentUserRentInstance();
        List<ArchiveFileInfo> archiveList = DstClient.getInstance().getArchiveList(instance);
        return archiveList;
    }

    @Override
    public void renameArchive(String fileName, String newFileName) {
        InstanceDO instance = getCurrentUserRentInstance();

        DstClient.getInstance().renameArchive(instance, fileName, newFileName);
    }

    @Override
    public void zipArchive() {

        RentDO rentEntity = getCurrentUserRentInfo();
        InstanceDO instance = getCurrentUserRentInstance();

        DstClient client = DstClient.getInstance();
        String fileName = "";
        try {
            WorldInfo worldInfo = client.getWorldInfo(instance);
            int cycles = worldInfo.getClock().getCycles();
            String nowDateTime = DateFormatUtil.formatDateTime(new Date(), DateFormatUtil.Pattern._4);
            fileName = "存档备份__租约ID" + rentEntity.getId() + "__世界第" + (cycles + 1) + "天__" + nowDateTime + ".zip";
        } catch (Exception exception) {
            fileName = "";
        }
        client.zipArchive(instance, fileName);
    }

    @Override
    public void unzipArchive(String fileName) {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();
        client.unzipArchive(instance, fileName);
        try {
            ClusterConfig clusterConfig = getClusterConfig();
            Integer maxPlayers = clusterConfig.getMaxPlayers();
            if (maxPlayers == null) {
                maxPlayers = 2;
            }
            Integer playerLimit = getCurrentUserRentInfo().getPlayerLimit();
            if (maxPlayers > playerLimit) {
                clusterConfig.setMaxPlayersLimit(playerLimit);
            }
            Boolean lanOnlyCluster = clusterConfig.getLanOnlyCluster();
            if (lanOnlyCluster == null || lanOnlyCluster == true) {
                clusterConfig.setLanOnlyCluster(false);
            }
            saveClusterConfig(clusterConfig);

            // todo 解析新档的 modoverride.lua, 把其中的模组ID取出来放到 dedicated_server_mods_setup.lua
            // 1. 清空dedicated_server_mods_setup.lua
//            client.clearModCache(instance);
            // 2. 获取modoverride.lua的全部模组ID
//            client.getCurrentModOverrides(instance);
            // 3. 全部模组ID写入到dedicated_server_mods_setup.lua
            // 4. 执行更新

        } catch (Exception ignore) {
        }
    }

    @Override
    public List<String> getCurrentModOverrides() {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();
        List<ModOverride> currentModOverrides = client.getCurrentModOverrides(instance);
        List<String> modIds = currentModOverrides.stream().map(ModOverride::getModId).collect(Collectors.toList());
        return modIds;
    }

    @Override
    public void flushModsIdIntoSetupFile() {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient client = DstClient.getInstance();
        List<ModOverride> currentModOverrides = client.getCurrentModOverrides(instance);
        if (CollectionUtils.isEmpty(currentModOverrides)) {
            return;
        }
        List<String> modIds = new ArrayList<>();
        for (ModOverride currentModOverride : currentModOverrides) {
            String modId = currentModOverride.getModId();
            modId = modId.replaceAll("workshop-", "");
            modIds.add(modId);
        }
        String modIdStr = String.join(",", modIds);
        client.FlushModsIdIntoSetupFile(instance, modIdStr);
    }

    @Override
    public void removeArchive(String fileName) {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient.getInstance().removeArchive(instance, fileName);
    }

    @Override
    public AgentInfo getAgentUrl() {
        InstanceDO instance = getCurrentUserRentInstance();
        String instanceIp = instance.getIp();
        Integer agentPort = instance.getAgentPort();
        Integer agentWsPort = instance.getAgentWsPort();
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setHttpUrl("http://" + instanceIp + ":" + agentPort);
        agentInfo.setHttpPort(agentPort);
        agentInfo.setWsUrl("ws://" + instanceIp + ":" + agentWsPort + "/socket");
        agentInfo.setWsPort(agentWsPort);
        return agentInfo;
    }

    @Override
    public void updateGame() {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient.getInstance().updateGame(instance);
    }

    @Override
    public void updateAllMods() {
        InstanceDO instance = getCurrentUserRentInstance();
        DstClient.getInstance().updateAllMods(instance);
    }

    @Override
    public List<SessionMeta> listSessionMetaFiles() {
        InstanceDO instance = getCurrentUserRentInstance();
        List<SessionMeta> sessionMetas = DstClient.getInstance().listSessionMetaFiles(instance);
        List<SessionMeta> responses = new ArrayList<>();
        int index = 0;
        int number = 1;
        for (int i = sessionMetas.size() - 1; i >= 0; i--) {
            SessionMeta sessionMeta = sessionMetas.get(i);
            SessionMeta response = new SessionMeta();
            BeanUtils.copyProperties(sessionMeta, response);
            response.setIndex(index);
            response.setNumber(number);
            index++;
            number++;
            responses.add(response);
        }
        return responses;
    }

    @Override
    public void checkModSteamLatestVersionNow(String modId) {
        modService.updateDbMod(modId);
    }

    @Override
    public DstGameInfo getLatestVersion() {
        DictDataRespDTO dictDataRespDTO = dictDataApi.parseDictData("dst_latest_version", "dst_latest_version");
        String latestVersion = dictDataRespDTO.getValue();
        DstGameInfo gameInfo = new DstGameInfo();
        gameInfo.setLatestVersion(latestVersion);
        return gameInfo;
    }


}
