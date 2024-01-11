package cn.iocoder.yudao.module.game.service.console.clients.dst.model;

import lombok.Data;

import java.util.Map;

/**
 * 存档添加的的模组配置. 前端也需按照这个实体类的结构组装模组配置.
 */
@Data
public class ModOverride {

    private String modId;

    private Map<String, Object> configuration_options;

    private Boolean enabled;
}
