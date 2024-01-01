package cn.iocoder.yudao.module.game.dal.dataobject.gamepackage;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 饥荒套餐配置 DO
 *
 * @author 管理员
 */
@TableName("game_package")
@KeySequence("game_package_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PackageDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 游戏对应code
     */
    private String gameCode;
    /**
     * 套餐版本
     */
    private Integer packageVersion;
    /**
     * 套餐编号
     */
    private Integer packageId;
    /**
     * 套餐名称
     */
    private String name;
    /**
     * 套餐描述
     */
    private String desc;
    /**
     * 原价格元/月
     */
    private Double price;
    /**
     * 玩家人数
     */
    private Integer player;
    /**
     * 核心数
     */
    private Integer cpu;
    /**
     * 内存,单位G(上限)
     */
    private Double memoryMax;
    /**
     * 内存,单位G(下限)
     */
    private Double memoryMin;
    /**
     * cpu主频率(下限)
     */
    private Double cpuHzMin;
    /**
     * cpu主频率(上限)
     */
    private Double cpuHzMax;

}