CREATE TABLE `game_uid_pool`
(
    `id`          bigint(11) NOT NULL COMMENT '自增主键',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
    `create_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1)       NOT NULL                                        DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户id池子';


CREATE TABLE `game_idc_host`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `ip`          varchar(255) NOT NULL COMMENT '主机IP/域名',
    `cert_path`   varchar(255) NOT NULL COMMENT '证书路径',
    `creator`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
    `create_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1)       NOT NULL                                        DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机房主机';


CREATE TABLE `game_instance`
(
    `id`                        bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `sale`                      tinyint(1) DEFAULT '0' COMMENT '是否已售, 0:未售, 1:已售',
    `company_code`              varchar(50)                                                  DEFAULT NULL COMMENT '云厂商code',
    `way_of_buy`                varchar(100)                                                 DEFAULT NULL COMMENT '购买方式/渠道',
    `is_own`                    tinyint(1) DEFAULT '1' COMMENT '是否是我们自己的机器, 针对dst套餐1所设置',
    `resource_account`          varchar(100)                                                 DEFAULT NULL COMMENT '云厂商实例对应账户',
    `resource_account_password` varchar(100)                                                 DEFAULT NULL COMMENT '云厂商实例对应账户密码',
    `ip`                        varchar(50) NOT NULL COMMENT '公网ip',
    `ssh_port`                  int(11) DEFAULT '22' COMMENT 'ssh 端口',
    `ssh_password`              varchar(50)                                                  DEFAULT 'Sunchen176&401_1' COMMENT 'ssh root密码',
    `region`                    varchar(50)                                                  DEFAULT '未知' COMMENT '服务器所处地域',
    `memory`                    double      NOT NULL COMMENT '内存,单位G',
    `cpu`                       int(11) NOT NULL COMMENT '核心数,单位G',
    `cpu_hz`                    double                                                       DEFAULT NULL COMMENT 'cpu频率',
    `disk`                      int(11) DEFAULT NULL COMMENT '磁盘容量',
    `disk_used_percent`         int(11) DEFAULT NULL COMMENT '磁盘容量剩余百分比',
    `package_name`              varchar(100)                                                 DEFAULT NULL COMMENT '所属套餐名称',
    `price`                     varchar(50)                                                  DEFAULT '0' COMMENT '服务器成本价格',
    `start_time`                timestamp NULL DEFAULT NULL COMMENT '购置服务器起租时间',
    `end_time`                  timestamp NULL DEFAULT NULL COMMENT '购置服务器到期时间',
    `rent_end_time`             timestamp NULL DEFAULT NULL COMMENT '用户承租到期时间',
    `agent_start_time`          timestamp NULL DEFAULT NULL COMMENT '通用agent最近一次启动时间',
    `agent_heartbeat_time`      timestamp NULL DEFAULT NULL COMMENT '通用agent最近一次心跳时间',
    `agent_status`              varchar(10) NOT NULL                                         DEFAULT 'down' COMMENT '通用agent状态, up, down',
    `agent_port`                int(11) DEFAULT '18188' COMMENT '云控agent端口',
    `agent_ws_port`             int(11) DEFAULT '18189' COMMENT '云控agent websocket端口',
    `agent_version`             varchar(20)                                                  DEFAULT '0' COMMENT 'agent version版本号',
    `port_dst_cluster`          int(11) DEFAULT '10888' COMMENT '饥荒集群端口',
    `port_dst_master`           int(11) DEFAULT '10999' COMMENT '饥荒主世界端口',
    `port_dst_caves`            int(11) DEFAULT '10998' COMMENT '饥荒洞穴端口',
    `port_7daydie`              int(11) DEFAULT '26900' COMMENT '七日杀世界端口',
    `game_version_dst`          varchar(100)                                                 DEFAULT NULL COMMENT '饥荒游戏版本，为空表示未安装该游戏',
    `game_version_7daydie`      varchar(100)                                                 DEFAULT NULL COMMENT '7日杀游戏版本，为空表示未安装该游戏',
    `notes`                     text                                                         DEFAULT NULL COMMENT '备注',
    `create_time`               datetime    NOT NULL                                         DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`                   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time`               datetime    NOT NULL                                         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`                   bit(1)      NOT NULL                                         DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`                 bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='机器信息';


CREATE TABLE `game_package`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `game_code`       varchar(255) NOT NULL COMMENT '游戏对应code',
    `package_version` int(11) DEFAULT '1' COMMENT '套餐版本',
    `package_id`      int(11) DEFAULT '0' COMMENT '套餐编号',
    `name`            varchar(255)                                                 DEFAULT NULL COMMENT '套餐名称',
    `desc`            varchar(1000)                                                DEFAULT NULL COMMENT '套餐描述',
    `price`           double       NOT NULL COMMENT '原价格元/月',
    `player`          int(11) DEFAULT '0' COMMENT '玩家人数',
    `cpu`             int(11) DEFAULT NULL COMMENT '核心数',
    `memory_max`      double                                                       DEFAULT NULL COMMENT '内存,单位G(上限)',
    `memory_min`      double                                                       DEFAULT NULL COMMENT '内存,单位G(下限)',
    `cpu_hz_min`      double                                                       DEFAULT NULL COMMENT 'cpu主频率(下限)',
    `cpu_hz_max`      double                                                       DEFAULT NULL COMMENT 'cpu主频率(上限)',
    `create_time`     datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time`     datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         bit(1)       NOT NULL                                        DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`       bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='饥荒套餐配置';


CREATE TABLE `game_player`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `game_code`   varchar(255) NOT NULL COMMENT '游戏对应code',
    `user_id`     bigint(20) NOT NULL COMMENT '用户id',
    `rent_id`     bigint(20) NOT NULL COMMENT '用户租约id',
    `instance_ip` varchar(50)                                                  DEFAULT NULL COMMENT '机器ip',
    `number`      int(11) DEFAULT '0' COMMENT '玩家人数',
    `create_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1)       NOT NULL                                        DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY           `t_dst_player_user_id_IDX` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录局内玩家人数';


CREATE TABLE `game_operate_log`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `game_code`   varchar(255) NOT NULL COMMENT '游戏对应code',
    `user_id`     bigint(20) NOT NULL COMMENT '用户id',
    `rent_id`     bigint(20) DEFAULT NULL COMMENT '租约id',
    `instance_id` bigint(20) DEFAULT NULL COMMENT '机器id',
    `instance_ip` varchar(50)                                                  DEFAULT NULL COMMENT '公网ip',
    `name`        varchar(255)                                                 DEFAULT NULL COMMENT '操作名称',
    `create_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1)       NOT NULL                                        DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    KEY           `idx_uid` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志';


CREATE TABLE `game_snapshot`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `game_code`   varchar(255) NOT NULL COMMENT '游戏对应code',
    `action`      varchar(100)                                                 DEFAULT NULL COMMENT '执行动作',
    `snapshot`    text COMMENT '快照内容',
    `create_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time` datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1)       NOT NULL                                        DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`   bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='快照, 便于误操作恢复数据';


-- dontstarve.user_rent definition

CREATE TABLE `game_rent`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`         varchar(100) NOT NULL COMMENT '房间名称/自定义租约名称',
    `user_id`      bigint(20) DEFAULT NULL COMMENT '登录账号',
    `sale`         tinyint(1) DEFAULT '0' COMMENT '是否已售, 0:未售, 1:已售',
    `package_id`   bigint(20) DEFAULT NULL COMMENT '套餐ID',
    `game_code`    varchar(255)                                                 DEFAULT NULL COMMENT '游戏对应code',
    `player_limit` int(11) DEFAULT NULL COMMENT '最大玩家人数',
    `start_time`   timestamp NULL DEFAULT NULL COMMENT '租约开始时间',
    `end_time`     timestamp NULL DEFAULT NULL COMMENT '租约到期时间',
    `instance_id`  bigint(20) NOT NULL COMMENT '机器id',
    `instance_ip`  varchar(50)  NOT NULL COMMENT '机器ip',
    `cluster_name` varchar(100) NOT NULL COMMENT '(饥荒)世界名称',
    `notes`        text                                                         DEFAULT NULL COMMENT '备注',
    `auto_renewal` int(2) NOT NULL DEFAULT 0 COMMENT '是否开启自动续费',
    `create_time`  datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time`  datetime     NOT NULL                                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`      bit(1)       NOT NULL                                        DEFAULT b'0' COMMENT '是否删除',
    `tenant_id`    bigint(20) NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='租约表';


