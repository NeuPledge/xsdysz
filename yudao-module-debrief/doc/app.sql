DROP TABLE IF EXISTS `debrief_dic_branch`;
CREATE TABLE `debrief_dic_branch`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `branch_name` varchar(255) NOT NULL COMMENT '支部名',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='支部字典列表';

DROP TABLE IF EXISTS `debrief_dic_college`;
CREATE TABLE `debrief_dic_college`
(
    `id`           bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `college_name` varchar(255) NOT NULL COMMENT '学院名',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='学院字典列表';

DROP TABLE IF EXISTS `debrief_dic_grade`;
CREATE TABLE `debrief_dic_grade`
(
    `id`         bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `college_id` bigint(11) NOT NULL COMMENT '学院id',
    `grade_name` varchar(255) NOT NULL COMMENT '班级名',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='班级字典列表';

DROP TABLE IF EXISTS `debrief_evaluate_result`;
CREATE TABLE `debrief_evaluate_result`
(
    `id`              bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `party_member_id` bigint(11) NOT NULL COMMENT '被评议党员id(debrief_party_member表的主键)',
    `commenter_id`    bigint(11) NOT NULL COMMENT '评议者id（debrief_student表的主键）',
    `comment`         bigint(11) NOT NULL COMMENT '1.满意; 2.基本满意; 3.不满意',
    `branch_id`       bigint(11) DEFAULT NULL COMMENT '支部id',
    `college_id`      bigint(11) NOT NULL COMMENT '学院id',
    `grade_id`        bigint(11) NOT NULL COMMENT '班级id',
    `content`         varchar(255) DEFAULT NULL COMMENT '其他评价内容',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='评价结果表';


DROP TABLE IF EXISTS `debrief_party_member`;
CREATE TABLE `debrief_party_member`
(
    `id`                 bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `debrief_student_id` bigint(11) DEFAULT NULL COMMENT '学生id',
    `name`               varchar(255) NOT NULL COMMENT '学生姓名',
    `student_number`     varchar(255) NOT NULL COMMENT '学号',
    `branch_id`          bigint(11) DEFAULT NULL COMMENT '支部id',
    `college_id`         bigint(11) NOT NULL COMMENT '学院id',
    `grade_id`           bigint(11) NOT NULL COMMENT '班级id',
    `satisfaction`       bigint(11) DEFAULT '0' COMMENT '满意数',
    `base_satisfaction`  bigint(11) DEFAULT '0' COMMENT '基本满意数',
    `dissatisfaction`    bigint(11) DEFAULT '0' COMMENT '不满意数',
    `modify_time`        datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='党员信息表';

DROP TABLE IF EXISTS `debrief_student`;
CREATE TABLE `debrief_student`
(
    `id`             bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `name`           varchar(255) NOT NULL COMMENT '学生姓名',
    `student_number` varchar(255) NOT NULL COMMENT '学号',
    `grade_id`       bigint(11) DEFAULT NULL COMMENT '班级id',
    `grade_name`     varchar(255) DEFAULT NULL COMMENT '班级',
    `college_id`     bigint(11) NOT NULL COMMENT '学院id',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='学生信息表';

DROP TABLE IF EXISTS `primary_student`;
CREATE TABLE `primary_student`
(
    `id`             int(11) NOT NULL AUTO_INCREMENT,
    `college`        varchar(255) DEFAULT NULL,
    `branch`         varchar(255) DEFAULT NULL,
    `name`           varchar(255) DEFAULT NULL,
    `student_number` varchar(255) DEFAULT NULL,
    `grade`          varchar(255) DEFAULT NULL,
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
