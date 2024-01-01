package cn.iocoder.yudao.module.game.dal.dataobject.idchost;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 机房主机 DO
 *
 * @author 管理员
 */
@TableName("game_idc_host")
@KeySequence("game_idc_host_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdcHostDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 主机IP/域名
     */
    private String ip;
    /**
     * 证书路径
     */
    private String certPath;

}