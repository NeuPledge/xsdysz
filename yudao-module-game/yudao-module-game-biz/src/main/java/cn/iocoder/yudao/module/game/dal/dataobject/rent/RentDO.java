package cn.iocoder.yudao.module.game.dal.dataobject.rent;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 租约 DO
 *
 * @author 管理员
 */
@TableName("game_rent")
@KeySequence("game_rent_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 登录账号
     */
    private Long userId;
    /**
     * 是否已售, 0:未售, 1:已售
     */
    private Boolean sale;
    /**
     * 套餐ID
     */
    private Long packageId;
    /**
     * 游戏对应code
     */
    private String gameCode;
    /**
     * 最大玩家人数
     */
    private Integer playerLimit;
    /**
     * 租约开始时间
     */
    private LocalDateTime startTime;
    /**
     * 租约到期时间
     */
    private LocalDateTime endTime;
    /**
     * 机器id
     */
    private Long instanceId;
    /**
     * 机器ip
     */
    private String instanceIp;
    /**
     * 饥荒世界名称
     */
    private String clusterName;
    /**
     * 备注
     */
    private String notes;
    /**
     * 是否开启自动续费
     */
    private Integer autoRenewal;

}