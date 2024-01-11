package cn.iocoder.yudao.module.game.service.console.clients.dst;

import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;
import cn.iocoder.yudao.module.game.framework.HeaderHolder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import cn.iocoder.yudao.module.game.common.consts.AppException;
import cn.iocoder.yudao.module.game.common.consts.Constant;
import cn.iocoder.yudao.module.game.common.enums.dst.DayPhase;
import cn.iocoder.yudao.module.game.common.enums.dst.DstCharacterMap;
import cn.iocoder.yudao.module.game.common.enums.dst.DstCharacterMap.Character;
import cn.iocoder.yudao.module.game.common.enums.dst.LogType;
import cn.iocoder.yudao.module.game.common.enums.dst.Season;
import cn.iocoder.yudao.module.game.common.enums.dst.WorldType;
import cn.iocoder.yudao.module.game.common.response.ClusterConfig;
import cn.iocoder.yudao.module.game.common.response.HostResourceInfo;
import cn.iocoder.yudao.module.game.service.console.clients.dst.model.*;
import cn.iocoder.yudao.module.game.util.Assert;
import cn.iocoder.yudao.module.game.util.LuaUtil;
import cn.iocoder.yudao.module.game.util.MathUtil;
import cn.iocoder.yudao.module.game.util.OKHttpUtil;

import java.io.*;
import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.Profile.Section;
import org.springframework.util.StringUtils;

@Slf4j
public class DstClient {


    private static final DstClient client = new DstClient();

    private DstClient() {
    }

    public static DstClient getInstance() {
        return client;
    }

    public Map<String, String> buildHeader() {
        RentDO currentUserRent = HeaderHolder.getCurrentUserRent();
        Map<String, String> header = new HashMap<>();
        if (currentUserRent != null) {
            header.put(Constant.HEADER_USER_ID, currentUserRent.getUserId().toString());
            header.put(Constant.HEADER_RENT_ID, currentUserRent.getId().toString());
            header.put(Constant.DST_CLUSTER_NAME, currentUserRent.getClusterName());
        }
        return header;
    }

    public String sendGet(String url) {
        return sendGet(url, null);
    }

    public String sendGet(String url, Map<String, String> param) {

        return sendGet(url, param, null);
    }

    private String sendGet(String url, Map<String, String> param, Map<String, String> header) {
        if (header == null) {
            header = buildHeader();
        } else {
            Map<String, String> newHeader = buildHeader();
            for (String key : header.keySet()) {
                String value = header.get(key);
                newHeader.put(key, value);
            }
        }

        return OKHttpUtil.sendGet(url, param, header);
    }

    private String sendPostJsonObjectNoHeader(String url, Map<String, Object> paramMap) {
        Map<String, String> header = buildHeader();
        return OKHttpUtil.sendPostJsonObject(url, paramMap, header);
    }

    public String sendPost(String url, Map<String, String> paramMap) {
        Map<String, String> header = buildHeader();
        return OKHttpUtil.sendPost(url, paramMap, header);
    }

    public String sendPostJson(String url, Map<String, String> paramMap) {
        Map<String, String> header = buildHeader();
        return OKHttpUtil.sendPostJson(url, paramMap, header);
    }


    private String assemblyUrl(String ip, String path) {
        return String.format("http://%s:%d%s", ip, 18188, path);
    }

    private String assemblyUrlWithPort(String ip, Integer port, String path) {
        return String.format("http://%s:%d%s", ip, port, path);
    }

    private String assemblyUrl(InstanceDO instance, String path) {
        return String.format("http://%s:%d%s", instance.getIp(), instance.getAgentPort(), path);
    }

    public WorldInfo getWorldInfo(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/current-world-info"));
        if (response == null) {
            return new WorldInfo();
        }
        ClientResponse<String> clientResponse = JSON.parseObject(response, ClientResponse.class);
        String data = clientResponse.getData();
//        Assert.notNull(data, "查询世界信息失败.");
        if (data == null) {
            return new WorldInfo();
        }
        data = data.replaceAll("return ", "");
        data = data.replaceAll(" ", "");//  -.- 很奇怪为啥会有这么个玩意带回来.{"code":200,"data":"return {clock={},seasons={}}\u0000","message":"SUCCESS"}
        // 此时是一个非标准化的json，需要转成标准json
        String stdJson = data.replaceAll(",", ",\"");
        stdJson = stdJson.replaceAll("=", "\":");
        stdJson = stdJson.replaceAll("\\{", "{\"");

        stdJson = stdJson.replaceAll("\\{\"}", "{}");

        stdJson = stdJson.replaceAll("\\[\"", "");
        stdJson = stdJson.replaceAll("\"\\]", "");

        WorldInfo worldInfo = JSON.parseObject(stdJson, WorldInfo.class);

        // 第一天无任何信息时, 需要补一下默认值.
        if (worldInfo.getClock() != null && worldInfo.getClock().getPhase() == null) {
            worldInfo.getClock().setPhase(DayPhase.day.name());
        }
        if (worldInfo.getSeasons() != null && worldInfo.getSeasons().getSeason() == null) {
            worldInfo.getSeasons().setSeason(Season.autumn.name());
        }

        return worldInfo;
    }

    public WorldStatus queryWorldsStatus(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/world-status"));
        Assert.notNull(response, "查询世界状态失败.");
        ClientResponse<WorldStatus> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });

        WorldStatus worldStatus = clientResponse.getData();
        Assert.notNull(worldStatus, "查询世界状态失败.");
        return worldStatus;
    }

    public void startMaster(InstanceDO instance) {
        log.info("ip:{}, 开启地面世界", instance.getIp());
        Map<String, String> param = new HashMap<>();
        param.put("token", "ZnVja3lvdWJhZG1hbjExMA");
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/master/restart"), param);
        Assert.notNull(response, "开启地面世界失败.");
    }

    public void stopMaster(InstanceDO instance) {
        log.info("ip:{}, 关闭地面世界", instance.getIp());
        Map<String, String> param = new HashMap<>();
        param.put("token", "ZnVja3lvdWJhZG1hbjExMA");
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/master/stop"), param);
        Assert.notNull(response, "关闭地面世界失败.");
    }

    public void startCaves(InstanceDO instance) {
        log.info("ip:{}, 开启洞穴世界", instance.getIp());
        Map<String, String> param = new HashMap<>();
        param.put("token", "ZnVja3lvdWJhZG1hbjExMA");
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/caves/restart"), param);
        Assert.notNull(response, "开启洞穴世界失败.");
    }

    public void stopCaves(InstanceDO instance) {
        log.info("ip:{}, 关闭洞穴世界", instance.getIp());
        Map<String, String> param = new HashMap<>();
        param.put("token", "ZnVja3lvdWJhZG1hbjExMA");
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/caves/stop"), param);
        Assert.notNull(response, "关闭洞穴世界失败.");
    }

    public List<Player> getOnlinePlayers(InstanceDO instance) {
        Map<String, String> param = new HashMap<>();
        param.put("eventType", "c_listplayers");
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/tail-log"), param, null);
        Assert.notNull(response, "获取玩家列表失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, ClientResponse.class);
        String data = clientResponse.getData();
        Assert.notNull(data, "获取玩家列表失败.");
        List<Player> players = new ArrayList<>();
        if (!data.contains("KU_")) {
            return players;
        }
        data = data.replaceAll("\t", "");// 移除特殊符号
        String[] lines = data.split("\n");
        // [01:44:45]: *[1] (KU_SFlxVROJ) Zeera <wathgrithr>
        //            group(1)  group(2) group(3) group(4)
        Pattern pattern = Pattern.compile("\\[(\\d+)\\]\\s\\((.*)\\)\\s(.*)\\s\\<(.*)\\>");
        for (String line : lines) {
            if (!line.contains("KU_")) {
                continue;
            }
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                Player player = new Player();
                try {
                    player.setNumber(Integer.parseInt(matcher.group(1)));
                } catch (IndexOutOfBoundsException ignore) {
                    player.setNumber(null);
                }
                try {
                    player.setKleiId(matcher.group(2));
                } catch (IndexOutOfBoundsException ignore) {
                    player.setKleiId(null);
                }
                try {
                    player.setNickname(matcher.group(3));
                } catch (IndexOutOfBoundsException ignore) {
                    player.setNickname(null);
                }
                try {
                    String characterCode = matcher.group(4);
                    Character character = DstCharacterMap.map.get(characterCode);
                    if (character == null) {
                        player.setCharacter(characterCode);
                    } else {
                        player.setCharacter(character.getChineseName());
                    }
                } catch (IndexOutOfBoundsException ignore) {
                    player.setCharacter(null);
                }
                players.add(player);
            }
        }
        return players;
    }

    /**
     * 返回原始文本内容, 不做任何解析.
     */
    public String getClusterIniRaw(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/read-cluster-ini"));
        Assert.notNull(response, "读取配置失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, ClientResponse.class);
        String data = clientResponse.getData();
        return data;
    }

    // 读取cluster.ini
    public ClusterIni getClusterIni(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/read-cluster-ini"));
        Assert.notNull(response, "读取配置失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, ClientResponse.class);
        String data = clientResponse.getData();

        try {
            Ini ini = new Ini(new StringReader(data));
            String json = JSON.toJSONString(ini);
            ClusterIni clusterIni = JSON.parseObject(json, ClusterIni.class);
            return clusterIni;
        } catch (IOException e) {
            throw new AppException("文件格式错误.");
        }
    }

    // 更新cluster.ini
    public void updateClusterIni(InstanceDO instance, ClusterConfig config) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/read-cluster-ini"));
        if (response == null) {
            throw new AppException("读取配置失败.");
        }
        ClientResponse<String> clientResponse = JSON.parseObject(response, ClientResponse.class);
        String data = clientResponse.getData();
        String content = "";
        Ini ini = new Ini();
        StringWriter writer = new StringWriter();
        try {
            ini = new Ini(new StringReader(data));
            Config conf = ini.getConfig();
            conf.setEscape(false);     // 禁止转义
            conf.setEmptyOption(true); // 未填写的item也会显示
            ini.setConfig(conf);

            ini.get("NETWORK").put("cluster_name", config.getRoomName());
//            ini.get("NETWORK").put("cluster_intention", config.getGameIntention());
            ini.get("NETWORK").put("cluster_password", config.getRoomPassword());
            ini.get("NETWORK").put("cluster_description", config.getRoomDescription());
            ini.get("NETWORK").put("whitelist_slots", config.getWhitelistSlots());
            ini.get("NETWORK").put("lan_only_cluster", config.getLanOnlyCluster());

            ini.get("GAMEPLAY").put("game_mode", config.getGameMode());
            ini.get("GAMEPLAY").put("max_players", config.getMaxPlayers());
            ini.get("GAMEPLAY").put("pvp", config.getPvp());
            ini.get("GAMEPLAY").put("pause_when_empty", config.getPauseWhenEmpty());

            if (config.getClusterPort() != null) {
                ini.get("SHARD").put("master_port", config.getClusterPort());
            }

            // 群组服务器添加/移除
            if (config.getSteamGroupEnabled() != null && config.getSteamGroupEnabled()) {
                Section steam = ini.get("STEAM");
                if (steam == null) {
                    steam = ini.add("STEAM");
                }
                steam.put("steam_group_id", config.getSteamGroupId());
                steam.put("steam_group_only", config.getSteamGroupOnly());
                steam.put("steam_group_admins", config.getSteamGroupAdmins());
            } else {
                Section steam = ini.get("STEAM");
                if (steam != null) {
                    ini.remove(steam);
                }
            }

            ini.store(writer);

            content = writer.toString();

        } catch (IOException e) {
            throw new AppException("文件格式错误.");
        }
        if (!StringUtils.hasText(content)) {
            throw new AppException("配置内容为空.");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("content", content);
        String saveResponse = sendPostJsonObjectNoHeader(assemblyUrl(instance, "/dst-agent/api/write-cluster-ini"), paramMap);
        if (saveResponse == null) {
            throw new AppException("保存失败.");
        }
    }


    /**
     * 返回原始文本内容, 不做任何解析.
     */
    public String getMasterIniRaw(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/master/read-master-ini"));
        Assert.notNull(response, "读取配置失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, ClientResponse.class);
        String data = clientResponse.getData();
        return data;
    }


    // 读取master/server.ini
    public MasterServerIni getMasterIni(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/master/read-master-ini"));
        if (response == null) {
            return null;
        }
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        String data = clientResponse.getData();
        try {
            Ini ini = new Ini(new StringReader(data));
            String json = JSON.toJSONString(ini);
            MasterServerIni masterServerIni = JSON.parseObject(json, MasterServerIni.class);
            return masterServerIni;
        } catch (IOException e) {
            throw new AppException("文件格式错误，请联系客服为您处理~");
        }
    }

    // 更新master/server.ini
    public void updateMasterIni(InstanceDO instance, MasterServerIni serverIni) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/master/read-master-ini"));
        if (response == null) {
            throw new AppException("读取配置失败.");
        }
        ClientResponse<String> clientResponse = JSON.parseObject(response, ClientResponse.class);
        String data = clientResponse.getData();
        String content = "";
        Ini ini = new Ini();
        StringWriter writer = new StringWriter();
        try {
            ini = new Ini(new StringReader(data));
            Config conf = ini.getConfig();
            conf.setEscape(false);     // 禁止转义
            conf.setEmptyOption(true); // 未填写的item也会显示
            ini.setConfig(conf);

            ini.get("NETWORK").put("server_port", serverIni.getNETWORK().getServer_port());
            ini.get("SHARD").put("is_master", serverIni.getSHARD().getIs_master());
            ini.get("ACCOUNT").put("encode_user_path", serverIni.getACCOUNT().getEncode_user_path());

            ini.store(writer);

            content = writer.toString();

        } catch (IOException e) {
            throw new AppException("文件格式错误.");
        }
        if (!StringUtils.hasText(content)) {
            throw new AppException("配置内容为空.");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("content", content);
        String saveResponse = sendPostJsonObjectNoHeader(assemblyUrl(instance, "/dst-agent/api/master/write-master-ini"), paramMap);
        if (saveResponse == null) {
            throw new AppException("保存失败.");
        }
    }

    /**
     * 返回原始文本内容, 不做任何解析.
     */
    public String getCavesIniRaw(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/caves/read-caves-ini"));
        Assert.notNull(response, "读取配置失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, ClientResponse.class);
        String data = clientResponse.getData();
        return data;
    }


    // 读取caves/server.ini
    public CavesServerIni getCavesIni(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/caves/read-caves-ini"));
        if (response == null) {
            return null;
        }
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        String data = clientResponse.getData();
        try {
            Ini ini = new Ini(new StringReader(data));
            String json = JSON.toJSONString(ini);
            CavesServerIni cavesServerIni = JSON.parseObject(json, CavesServerIni.class);
            return cavesServerIni;
        } catch (IOException e) {
            throw new AppException("文件格式错误，请联系客服为您处理~");
        }
    }


    // 更新 caves/server.ini
    public void updateCavesIni(InstanceDO instance, CavesServerIni serverIni) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/caves/read-caves-ini"));
        if (response == null) {
            throw new AppException("读取配置失败.");
        }
        ClientResponse<String> clientResponse = JSON.parseObject(response, ClientResponse.class);
        String data = clientResponse.getData();
        String content = "";
        Ini ini = new Ini();
        StringWriter writer = new StringWriter();
        try {
            ini = new Ini(new StringReader(data));
            Config conf = ini.getConfig();
            conf.setEscape(false);     // 禁止转义
            conf.setEmptyOption(true); // 未填写的item也会显示
            ini.setConfig(conf);

            ini.get("NETWORK").put("server_port", serverIni.getNETWORK().getServer_port());
            ini.get("SHARD").put("is_master", serverIni.getSHARD().getIs_master());
            ini.get("SHARD").put("name", serverIni.getSHARD().getName());
            ini.get("SHARD").put("id", serverIni.getSHARD().getId());
            ini.get("ACCOUNT").put("encode_user_path", serverIni.getACCOUNT().getEncode_user_path());
            ini.get("STEAM").put("master_server_port", serverIni.getSTEAM().getMaster_server_port());
            ini.get("STEAM").put("authentication_port", serverIni.getSTEAM().getAuthentication_port());

            ini.store(writer);

            content = writer.toString();

        } catch (IOException e) {
            throw new AppException("文件格式错误.");
        }
        if (!StringUtils.hasText(content)) {
            throw new AppException("配置内容为空.");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("content", content);
        String saveResponse = sendPostJsonObjectNoHeader(assemblyUrl(instance, "/dst-agent/api/caves/write-caves-ini"), paramMap);
        if (saveResponse == null) {
            throw new AppException("保存失败.");
        }
    }


    public String readPlayerFiles(InstanceDO instance, String type) {
        Map<String, String> param = new HashMap<>();
        // type: admin:管理员, block:黑名单, white:白名单
        param.put("type", type);
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/master/read-player-file"), param, null);
        if (response == null) {
            return null;
        }
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        return clientResponse.getData();
    }

    public String updatePlayerFiles(InstanceDO instance, String type, String content) {
        Map<String, String> param = new HashMap<>();
        // type: admin:管理员, block:黑名单, white:白名单
        param.put("type", type);
        param.put("content", content);
        String response = sendPost(assemblyUrl(instance, "/dst-agent/api/master/write-player-file"), param);
        if (response == null) {
            return null;
        }
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        return clientResponse.getData();
    }

    // TrackServerLog 检查server Log是否有报错日志
    public List<String> trackServerLog(InstanceDO instance, LogType logType) {
        Map<String, String> param = new HashMap<>();
        // LogType: master_log, cave_log
        param.put("logType", logType.name());
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/track-server-log"), param, null);
        Assert.notNull(response, "获取日志失败.");
        ClientResponse<List<String>> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "获取日志失败.");

        return clientResponse.getData();
    }

    public String getLog(InstanceDO instance, LogType logType, Integer line) {
        Map<String, String> param = new HashMap<>();
        // LogType: master_log, cave_log, chat_log
        param.put("logType", logType.name());
        param.put("line", line.toString());
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/read-log"), param, null);
        Assert.notNull(response, "获取日志失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "获取日志失败.");

        return clientResponse.getData();
    }


    // 传入任意命令执行(仅限饥荒控制台命令).
    public String executeCommand(InstanceDO instance, String command) {
        Map<String, String> param = new HashMap<>();
        param.put("command", command);
        String response = sendPostJson(assemblyUrl(instance, "/dst-agent/api/master/execute-command"), param);
        Assert.notNull(response, "操作失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "操作失败.");
        return clientResponse.getData();
    }

    // CheckDownloadAndGetConfig 检查模组是否存在，如果已经存在，则执行更新命令，并取回modinfo.lua的内容.
    public List<LocalModInfo> checkDownloadAndGetConfig(InstanceDO instance, String modIds) {
        Map<String, String> param = new HashMap<>();
        param.put("ids", modIds);// id 可以是多个, 用逗号分隔.
        String response = sendPostJson(assemblyUrl(instance, "/dst-agent/api/check-download-mod"), param);
        Assert.notNull(response, "操作失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "操作失败.");
        List<LocalModInfo> mods = LuaUtil.parseModOption(clientResponse.getData());
        // size = 1
        return mods;
    }

    // ReDownloadAndGetConfig 重新下载模组(仅支持单个模组)(具备循环检测并反复继续功能)
    public List<LocalModInfo> reDownloadAndGetConfig(InstanceDO instance, String modId) {
        Map<String, String> param = new HashMap<>();
        param.put("id", modId);// id 可以是多个, 用逗号分隔.
        String response = sendPostJson(assemblyUrl(instance, "/dst-agent/api/re-download-mod"), param);
        Assert.notNull(response, "操作失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "操作失败.");
        List<LocalModInfo> mods = LuaUtil.parseModOption(clientResponse.getData());
        // size = 1
        return mods;
    }

    // FlushSteamAcfFile cong acf文件中 清理某个模组
    public void flushSteamAcfFile(InstanceDO instance, String modId) {
        Map<String, String> param = new HashMap<>();
        param.put("id", modId);// id 可以是多个, 用逗号分隔.
        String response = sendPostJson(assemblyUrl(instance, "/dst-agent/api/flush-steam-acf-file"), param);
        Assert.notNull(response, "操作失败.");
    }

    public void deleteModFileFromServer(InstanceDO instance, String modIds) {
        Map<String, String> param = new HashMap<>();
        param.put("ids", modIds);// id 可以是多个, 用逗号分隔.
        String response = sendPostJson(assemblyUrl(instance, "/dst-agent/api/delete-mods"), param);
        Assert.notNull(response, "操作失败.");
    }


    // 将ID，全部刷入到dedicated_server_mods_setup.lua
    public void FlushModsIdIntoSetupFile(InstanceDO instance, String modIds) {
        Map<String, String> param = new HashMap<>();
        param.put("ids", modIds);// id 可以是多个, 用逗号分隔.
        String response = sendPostJson(assemblyUrl(instance, "/dst-agent/api/flush-mods-id-into-setup-file"), param);
        Assert.notNull(response, "操作失败.");
    }

    // 获取当前存档的模组配置信息
    public List<ModOverride> getCurrentModOverrides(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/master/read-mod-override"));
        Assert.notNull(response, "获取模组信息失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "获取模组信息失败.");

        String data = clientResponse.getData();
        /*data = data.replaceAll("return ", "");
        data = data.replaceAll("return", "");
        data = data.replaceAll(" ", "");
        data = data.replaceAll("\n", "");

        // 转成json
        // ={    ":{"
        // [
        // ]
        // ,    ,"
        // =     ":
        // ""   "
        data = data.replaceAll("=\\{", "\":{\"");
        data = data.replaceAll("\\[", "");
        data = data.replaceAll("]", "");
        data = data.replaceAll(",", ",\"");
        data = data.replaceAll("=", "\":");
        data = data.replaceAll("\"\"", "\"");
        data = data.replaceAll("\\{\"}", "{}");
        data = data.replaceAll(",\"\\}", "}");*/

//        log.info("mod:" + data);
        List<ModOverride> modOverrides = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(data);
        if (jsonObject == null) {
            return modOverrides;
        }
        for (String modId : jsonObject.keySet()) {
            ModOverride override = new ModOverride();
            override.setModId(modId);// workshop-2577742416
            JSONObject jsonObject1 = jsonObject.getJSONObject(modId);
            override.setEnabled(jsonObject1.getBoolean("enabled"));

            JSONObject configurationOptions = jsonObject1.getJSONObject("configuration_options");
            Map<String, Object> map = new HashMap<>();
            for (String key : configurationOptions.keySet()) {
                map.put(key, configurationOptions.get(key));
            }
            override.setConfiguration_options(map);
            modOverrides.add(override);
        }

        return modOverrides;
    }

    // 获取当前本地所有的模组
    public List<LocalModInfo> getLocalMods(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/local-mods"));
        Assert.notNull(response, "获取本地模组失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        String data = clientResponse.getData();
        Assert.notNull(data, "获取本地模组失败.");
//        log.info("getLocalMods:" + data);
        List<LocalModInfo> mods = LuaUtil.parseModOption(data);

        return mods;
    }

    public void clearModCache(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/clear-mod-cache"));
        Assert.notNull(response, "清除缓存失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Integer code = clientResponse.getCode();
        Assert.notNull(code == null || code != 200, "清除缓存失败.");
    }

    // 保存存档模组配置.
    public void saveModOverride(InstanceDO instance, String content) {
        Map<String, String> param = new HashMap<>();
        // type: admin:管理员, block:黑名单, white:白名单
        param.put("content", content);
        String response = sendPost(assemblyUrl(instance, "/dst-agent/api/master/write-mod-override"), param);
        Assert.notNull(response, "保存模组配置失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "保存模组配置失败.");
    }

    // 获取森林世界配置.
    public Map<String, String> readLevelOverride(InstanceDO instance, WorldType worldType) {
        HashMap<String, String> map = new HashMap<>();
        if (worldType == WorldType.master) {
            String response = sendGet(assemblyUrl(instance, "/dst-agent/api/master/read-world-config"));
            Assert.notNull(response, "获取森林世界配置失败.");
            ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
            });
            Assert.notNull(clientResponse, "获取森林世界配置失败.");

            map = JSON.parseObject(clientResponse.getData(), new TypeReference<>() {
            });
        } else {
            String response = sendGet(assemblyUrl(instance, "/dst-agent/api/caves/read-world-config"));
            Assert.notNull(response, "获取洞穴世界配置失败.");
            ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
            });
            Assert.notNull(clientResponse, "获取洞穴世界配置失败.");

            map = JSON.parseObject(clientResponse.getData(), new TypeReference<>() {
            });
        }

//        String raw = map.get("raw");
//        raw = raw.replaceAll("return[\s]+", "");
//        raw = raw.replaceAll("\\n\\s", "");
//        raw = raw.replaceAll("\\n", "");
//        raw = raw.replaceAll("\\{[\s]+", "{");
//        raw = raw.replaceAll("[\s]+}", "}");
//        raw = raw.replaceAll(",[\s]+", ",");
//        raw = raw.replaceAll("=\s", "=");
//
//        String newraw = raw.replaceAll("\\{", "{\"");
//
//        newraw = newraw.replaceAll("=", "\":");
//        newraw = newraw.replaceAll(",", ",\"");
//        newraw = newraw.replaceAll("[\"]+", "\"");
//        newraw = newraw.replaceAll("\\{\"}", "{}");
//        map.put("raw", newraw);
        return map;
    }

    public void saveLevelOverride(InstanceDO instance, Map<String, String> map, WorldType worldType) {
        String raw = map.get("raw");
        String override = map.get("override");
        override = override.replaceAll("\\{\"", "{");
        override = override.replaceAll("\":", "=");
        override = override.replaceAll(",\"", ",\n");

        String newRaw = raw.replaceAll("overrides=\\{[^\\}]+\\}", "overrides=" + override);

        Map<String, String> param = new HashMap<>();
        param.put("content", newRaw);// id 可以是多个, 用逗号分隔.
        if (worldType == WorldType.master) {
            String response = sendPostJson(assemblyUrl(instance, "/dst-agent/api/master/write-world-config"), param);
            Assert.notNull(response, "操作失败.");
        } else {
            String response = sendPostJson(assemblyUrl(instance, "/dst-agent/api/caves/write-world-config"), param);
            Assert.notNull(response, "操作失败.");
        }
    }

    public String getProcessInfo(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/process-info"));
        Assert.notNull(response, "从Agent获取进程信息失败");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });

        Assert.notNull(clientResponse, "解析进程信息失败.");

        return clientResponse.getData();
    }

    public List<ArchiveFileInfo> getArchiveList(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/list-all-archives"));
        Assert.notNull(response, "从Agent获取存档信息失败");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "解析存档信息失败.");

        String data = clientResponse.getData();
        List<ArchiveFileInfo> archiveFileInfos = JSON.parseArray(data, ArchiveFileInfo.class);
        if (archiveFileInfos == null || archiveFileInfos.size() == 0) {
            return new ArrayList<>();
        }
        for (ArchiveFileInfo archiveFileInfo : archiveFileInfos) {
            String sizeStr = archiveFileInfo.getSize();
            long size = Long.parseLong(sizeStr);
            if (size < 1024) {
                archiveFileInfo.setSize(size + "B");
            } else if (size < 1024 * 1024) {
                archiveFileInfo.setSize(MathUtil.roundUp((size / 1024.0), 1) + "K");
            } else if (size < 1024 * 1024 * 1024) {
                archiveFileInfo.setSize(MathUtil.roundUp((size / 1024.0 / 1024.0), 2) + "M");
            } else if (size < 1024L * 1024 * 1024 * 1024) {
                archiveFileInfo.setSize(MathUtil.roundUp((size / 1024.0 / 1024.0 / 1024.0), 2) + "G");
            }
        }
        List<ArchiveFileInfo> fileInfos = archiveFileInfos.stream().sorted(Comparator.comparing(ArchiveFileInfo::getTime).reversed()).collect(Collectors.toList());
        return fileInfos;
    }

    public void renameArchive(InstanceDO instance, String fileName, String newFileName) {
        Map<String, String> param = new HashMap<>();
        param.put("fileName", fileName);
        param.put("newFileName", newFileName);
        String response = sendPost(assemblyUrl(instance, "/dst-agent/api/rename-archive"), param);
        Assert.notNull(response, "重命名失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "重命名失败.");
    }

    /**
     * 创建备份压缩包.
     *
     * @param fileName 为空则使用默认命名规则创建.
     */
    public void zipArchive(InstanceDO instance, String fileName) {
        Map<String, String> param = new HashMap<>();
        param.put("fileName", fileName);
        String response = sendPost(assemblyUrl(instance, "/dst-agent/api/create-archive"), param);
        Assert.notNull(response, "创建存档失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "创建存档失败.");

    }

    /**
     * 解压存档.
     *
     * @param fileName 需要.zip结尾.
     */
    public void unzipArchive(InstanceDO instance, String fileName) {
        Map<String, String> param = new HashMap<>();
        param.put("fileName", fileName);
        String response = sendPost(assemblyUrl(instance, "/dst-agent/api/unzip-archive"), param);
        Assert.notNull(response, "解压存档失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "解压存档失败.");
    }


    // 查询服务器版本
    public String getDstVersion(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/dst-version"));
        if (response == null) {
            return "";
        }
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        if (clientResponse == null) {
            return "";
        }
        String data = clientResponse.getData();
        data = data.replaceAll("\n", "");
        return data;
    }

    // 更新游戏
    public void updateGame(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/update-game"));
        Assert.notNull(response, "更新游戏失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "更新游戏失败.");
    }

    // 更新所有模组
    public void updateAllMods(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/update-mods"));
        Assert.notNull(response, "更新模组失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "更新模组失败.");
    }

    public void removeArchive(InstanceDO instance, String fileName) {
        Map<String, String> param = new HashMap<>();
        param.put("fileName", fileName);
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/remove-archive"), param);
        Assert.notNull(response, "删除备份 失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "删除备份失败.");
    }

    public List<SessionMeta> listSessionMetaFiles(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/list-session-meta"));
        if (response == null) {
            return new ArrayList<>();
        }
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        if (response == null) {
            return new ArrayList<>();
        }
        String data = clientResponse.getData();

        List<SessionMeta> sessionMetas = new ArrayList<>();
        JSONArray metaList = JSON.parseArray(data);
        for (Object o : metaList) {
            JSONObject metaFile = (JSONObject) o;
            SessionMeta sessionMeta = new SessionMeta();
            sessionMeta.setFileName(metaFile.getString("fileName"));
            sessionMeta.setMasterSessionId(metaFile.getString("masterSessionId"));
            String fileContent = metaFile.getString("fileContent");
            fileContent = fileContent.replaceAll("return ", "");
            fileContent = fileContent.replaceAll(" ", "");//  -.- 很奇怪为啥会有这么个玩意带回来.{"code":200,"data":"return {clock={},seasons={}}\u0000","message":"SUCCESS"}
            // 此时是一个非标准化的json，需要转成标准json
            String stdJson = fileContent.replaceAll(",", ",\"");
            stdJson = stdJson.replaceAll("=", "\":");
            stdJson = stdJson.replaceAll("\\{", "{\"");

            stdJson = stdJson.replaceAll("\\{\"}", "{}");

            stdJson = stdJson.replaceAll("\\[\"", "");
            stdJson = stdJson.replaceAll("\"\\]", "");

            WorldInfo worldInfo = JSON.parseObject(stdJson, WorldInfo.class);

            // 第一天无任何信息时, 需要补一下默认值.
            if (worldInfo.getClock().getPhase() == null) {
                worldInfo.getClock().setPhase(DayPhase.day.name());
            }
            if (worldInfo.getSeasons().getSeason() == null) {
                worldInfo.getSeasons().setSeason(Season.autumn.name());
            }

            sessionMeta.setCycles(worldInfo.getClock().getCycles());
            sessionMeta.setCurrentDay(String.format("第%d天", worldInfo.getClock().getCycles() + 1));
            sessionMeta.setSeason(Season.valueOf(worldInfo.getSeasons().getSeason()).getName());
            sessionMeta.setPhase(worldInfo.getClock().getPhase());
            sessionMeta.setPhase_zh(DayPhase.valueOf(worldInfo.getClock().getPhase()).getName());
            sessionMeta.setPhaseRate(calculateDayRemains(worldInfo) + "%");
            sessionMetas.add(sessionMeta);
        }
        return sessionMetas;
    }


    public static double calculateDayRemains(WorldInfo worldInfo) {
        try {
            double totaltimeinphase = 480.0;
            String phase = worldInfo.getClock().getPhase();
            double remainingtimeinphase = worldInfo.getClock().getRemainingtimeinphase();
            double elapse = 0.0;
            if ("day".equals(phase)) {
                int day = worldInfo.getClock().getSegs().getDay();
                double total = 30 * day;
                elapse = (total - remainingtimeinphase) / totaltimeinphase;
            } else if ("dusk".equals(phase)) {
                int day = worldInfo.getClock().getSegs().getDay();
                int dusk = worldInfo.getClock().getSegs().getDusk();
                double total = 30 * (day + dusk);
                elapse = (total - remainingtimeinphase) / totaltimeinphase;
            } else if ("night".equals(phase)) {
                int day = worldInfo.getClock().getSegs().getDay();
                int dusk = worldInfo.getClock().getSegs().getDusk();
                int night = worldInfo.getClock().getSegs().getNight();

                double total = 30 * (day + dusk + night);
                elapse = (total - remainingtimeinphase) / totaltimeinphase;
            }
            double rate = MathUtil.roundUp(100 * elapse, 1);
            return rate;
        } catch (Exception exception) {
            return 0;
        }

    }

    // 更新agent本身.
    public void updateAgent(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/update-agent"));
        Assert.notNull(response, "操作失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "操作失败.");
    }

    // 强制重置世界, 删除相关文件后重启世界.(在用)
    public void forceReGenerateWorld(InstanceDO instance) {
        Map<String, String> param = new HashMap<>();
        param.put("token", "ZnVja3lvdWJhZG1hbjExMA");
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/force-regenerate-world"), param);
        Assert.notNull(response, "操作失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "操作失败.");
    }

    // 强制重置世界, 使用默认存档文件覆盖.(没在用)
    public void forceReGenerateWorld2(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/force-regenerate-world2"));
        Assert.notNull(response, "操作失败.");
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "操作失败.");
    }

    /**
     * 获取机器当前的监控信息
     */
    public HostResourceInfo getHostResourceInfo(InstanceDO instance) {
        String response = sendGet(assemblyUrl(instance, "/dst-agent/api/host-resource-info"));
        Assert.notNull(response, "操作失败.");
        log.info(response);
        ClientResponse<String> clientResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        Assert.notNull(clientResponse, "操作失败.");
        String data = clientResponse.getData();
        HostResourceInfo resourceInfo = JSON.parseObject(data, HostResourceInfo.class);
        Assert.notNull(resourceInfo, "JSON 解析异常");
        return resourceInfo;
    }
}
