package cn.iocoder.yudao.module.game.dal.dataobject.snapshot;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 快照, 便于误操作恢复数据 DO
 *
 * @author 管理员
 */
@TableName("game_snapshot")
@KeySequence("game_snapshot_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SnapshotDO extends BaseDO {

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
     * 执行动作
     */
    private String action;
    /**
     * 快照内容
     */
    private String snapshot;

}