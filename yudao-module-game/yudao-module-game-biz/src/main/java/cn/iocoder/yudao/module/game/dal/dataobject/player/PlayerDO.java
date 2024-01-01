package cn.iocoder.yudao.module.game.dal.dataobject.player;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 记录局内玩家人数 DO
 *
 * @author 管理员
 */
@TableName("game_player")
@KeySequence("game_player_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDO extends BaseDO {

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
     * 用户租约id
     */
    private Long rentId;
    /**
     * 机器ip
     */
    private String instanceIp;
    /**
     * 玩家人数
     */
    private Integer number;

}