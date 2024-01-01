
CREATE TABLE `game_dst_block_mod`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `mod_id`      bigint(20) DEFAULT NULL COMMENT '模组id(不含workshop-)',
    `mod_title`   varchar(255)                                                 DEFAULT NULL COMMENT '模组名称',
    `desc`        varchar(255)                                                 DEFAULT NULL COMMENT '拉黑理由',
    `status`      varchar(20)                                                  DEFAULT NULL COMMENT 'warn/deny',
    `create_time` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1)   NOT NULL                                            DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模组黑名单,此名单上的模组,将会警告提示/禁止添加';


CREATE TABLE `game_dst_mods`
(
    `id`               bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `mod_id`           bigint(20) DEFAULT NULL COMMENT '模组id(不含workshop-)',
    `mod_title`        varchar(255)                                                 DEFAULT NULL COMMENT '模组名称',
    `is_server_mod`    int(11) DEFAULT '0' COMMENT '是否为服务端模组',
    `url`              varchar(255) CHARACTER SET utf8                              DEFAULT NULL COMMENT '模组steam链接',
    `tags`             varchar(255) CHARACTER SET utf8                              DEFAULT NULL COMMENT '标签',
    `depend_mods`      varchar(100) CHARACTER SET utf8                              DEFAULT NULL COMMENT '依赖的模组',
    `image`            varchar(255) CHARACTER SET utf8                              DEFAULT NULL COMMENT '模组图标',
    `star_url`         varchar(255) CHARACTER SET utf8                              DEFAULT NULL COMMENT '模组评分星级图标',
    `author`           varchar(255)                                                 DEFAULT NULL COMMENT '作者',
    `version`          varchar(255) CHARACTER SET utf8                              DEFAULT NULL COMMENT '版本',
    `fileSize`         varchar(50) CHARACTER SET utf8                               DEFAULT NULL COMMENT '文件大小',
    `mod_publish_time` varchar(50) CHARACTER SET utf8                               DEFAULT NULL COMMENT '模组发布时间',
    `mod_update_time`  varchar(50) CHARACTER SET utf8                               DEFAULT NULL COMMENT '模组更新时间',
    `star`             int(11) DEFAULT NULL COMMENT '模组评分星级',
    `status`           int(11) DEFAULT '1' COMMENT '模组状态,0:不可用,1:可用',
    `create_time`      datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time`      datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`          bit(1)   NOT NULL                                            DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`        bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `index_mod_id` (`mod_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='饥荒模组信息';


CREATE TABLE `game_dst_world_option`
(
    `id`                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `world`             varchar(20)                                                  DEFAULT NULL COMMENT '世界类型master/caves',
    `config_type`       varchar(50)                                                  DEFAULT NULL COMMENT '配置类型, 世界选项/世界生成',
    `resource_type`     varchar(255)                                                 DEFAULT NULL COMMENT '分类(生物/资源/...)',
    `label`             varchar(255)                                                 DEFAULT NULL COMMENT '配置标题(human read)',
    `global_config`     int(11) DEFAULT '0' COMMENT '是否是全局配置',
    `status`            int(11) DEFAULT '1' COMMENT '启用',
    `sort`              int(11) DEFAULT NULL COMMENT '排序',
    `key_name`          varchar(255)                                                 DEFAULT NULL COMMENT '配置name(key)',
    `icon`              varchar(255)                                                 DEFAULT NULL COMMENT '图标',
    `options`           varchar(512)                                                 DEFAULT NULL COMMENT '可选项(json数组)',
    `default_option`    varchar(255)                                                 DEFAULT NULL COMMENT '默认选项',
    `recommend_option`  varchar(255)                                                 DEFAULT NULL COMMENT '推荐选项(同理可以添加其他类型的选项)',
    `relaxed_option`    varchar(100)                                                 DEFAULT NULL COMMENT '轻松模式下的选项',
    `endless_option`    varchar(100)                                                 DEFAULT NULL COMMENT '无尽模式下的选项',
    `survival_option`   varchar(100)                                                 DEFAULT NULL COMMENT '生存模式下的选项',
    `wilderness_option` varchar(100)                                                 DEFAULT NULL COMMENT '荒野模式下的选项',
    `lightsout_option`  varchar(100)                                                 DEFAULT NULL COMMENT '暗无天日模式下的选项',
    `create_time`       datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time`       datetime NOT NULL                                            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`           bit(1)   NOT NULL                                            DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`         bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='饥荒世界配置初始化数据';