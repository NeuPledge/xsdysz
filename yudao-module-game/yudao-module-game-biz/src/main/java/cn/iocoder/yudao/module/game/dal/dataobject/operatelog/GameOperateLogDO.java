package cn.iocoder.yudao.module.game.dal.dataobject.operatelog;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 操作日志 DO
 *
 * @author 管理员
 */
@TableName("game_operate_log")
@KeySequence("game_operate_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameOperateLogDO extends BaseDO {

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
     * 用户id
     */
    private Long userId;
    /**
     * 租约id
     */
    private Long rentId;
    /**
     * 机器id
     */
    private Long instanceId;
    /**
     * 公网ip
     */
    private String instanceIp;
    /**
     * 操作名称
     */
    private String name;

}