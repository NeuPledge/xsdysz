package cn.iocoder.yudao.module.game.service.console.clients.dst.model;

import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class LevelConfig {

    private String raw;

    private Map<String, List<LevelConfigItem>> optionResourceList;  // 世界选项
    private Map<String, List<LevelConfigItem>> generateResourceList;// 世界生成
    private List<LevelConfigItem> defaultOption;                    // 默认配置，前端无需展示. 但是需要取出值塞到保存的json中.

    @Data
    public static class LevelConfigItem {

        private Integer order;
        private String configType;
        private String resourceType;
        private String key;
        private String label;
        private String icon;
        private String defaultOption;
        private Object data;
        private List<LevelOption> options;
        private Integer globalConfig;
    }

    @Data
    public static class LevelOption {

        private String key;
        private String label;
    }
}
