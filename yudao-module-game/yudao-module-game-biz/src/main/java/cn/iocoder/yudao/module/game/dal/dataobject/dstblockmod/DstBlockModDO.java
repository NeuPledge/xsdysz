package cn.iocoder.yudao.module.game.dal.dataobject.dstblockmod;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 模组黑名单,此名单上的模组,将会警告提示/禁止添加 DO
 *
 * @author 管理员
 */
@TableName("game_dst_block_mod")
@KeySequence("game_dst_block_mod_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DstBlockModDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 模组id(不含workshop-)
     */
    private Long modId;
    /**
     * 模组名称
     */
    private String modTitle;
    /**
     * 拉黑理由
     */
    private String desc;
    /**
     * warn/deny
     */
    private String status;

}