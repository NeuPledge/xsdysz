package cn.iocoder.yudao.module.game.util;

import cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.module.game.service.console.clients.dst.model.LocalModInfo.ModConfig;

/**
 * json to lua table
 */
@Slf4j
public class LuaUtil {

    public static List<LocalModInfo> parseModOption(String json) {

        JSONArray objects = JSON.parseArray(json);
        List<LocalModInfo> mods = new ArrayList<>();
        if (objects == null) {
            return mods;
        }
        for (int i = 0; i < objects.size(); i++) {
            JSONObject jsonObject2 = (JSONObject) objects.get(i);
            int index = 1;
            try {
                String modId = jsonObject2.getString("modId");
                String serverVersion = jsonObject2.getString("version");
                String defaultOption = jsonObject2.getString("defaultOption");
                JSONObject jsonObject = null;
                if (!StringUtils.isEmpty(defaultOption)){
//                    defaultOption = StringEscapeUtils.unescapeJson(defaultOption);
                    defaultOption = defaultOption.replaceAll("\\\\","\\\\\\\\");
                }
                jsonObject = JSON.parseObject(defaultOption);

                List<LocalModInfo.ModConfig> configs = new ArrayList<>();
                if (jsonObject != null) {
                    while (true) {
                        ModConfig object = jsonObject.getObject(String.valueOf(index), ModConfig.class);
                        if (object == null) {
                            break;
                        }
                        configs.add(object);
                        index++;
                    }
                }
                LocalModInfo modInfo = new LocalModInfo();
                modInfo.setModId(modId);
                modInfo.setModConfigs(configs);
                modInfo.setServerVersion(serverVersion);
                mods.add(modInfo);
            } catch (Exception exception) {
                log.info("解析local mod出现异常模组:" + jsonObject2.toJSONString());
                log.info("解析local mod出现异常模组 index:" + index);
            }
        }
        return mods;
    }

}
