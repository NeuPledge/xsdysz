package cn.iocoder.yudao.module.game.service.console.clients.dst.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalModInfo extends SteamModInfo {

    // mod steam id, 不含"workshop-"
    private String modId;

    // 模组的多项配置(取自原始模组配置文件)
    @JSONField(name = "defaultOption")
    private List<ModConfig> modConfigs;

    // 模组自定义配置(经过前端配置过的), 和modConfigs类似，作用不同.
    private Map<String, Object> configuration_options;

    // 当前存档是否已启用该模组
    private Boolean enabled;

    private String serverVersion;// 服务器当前版本
    private String version; // steam当前版本, 便于比对

    @Data
    public static class ModConfig {

        private String name;
        private String label;
        private String hover;

        @SerializedName("default")
        @JSONField(name = "default")
        private Object defaultOptionData;// 存储值并非索引, 而是options中对应耳朵data值.

        private Integer optionSelectedIndex;// (存储的是索引序号, 可能为null, 为null时直接使用defaultOptionData值即可) 非原生, 用于存储已配置模组的自定义配置, 供前端展示以及数据组装. 若此值为空, 则默认展示defaultOptionIndex

        private List<Option> options;

        @Data
        public static class Option {

            private String description;
            private Object data;
            private String hover;
        }

    }


}
