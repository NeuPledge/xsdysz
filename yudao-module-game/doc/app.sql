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


