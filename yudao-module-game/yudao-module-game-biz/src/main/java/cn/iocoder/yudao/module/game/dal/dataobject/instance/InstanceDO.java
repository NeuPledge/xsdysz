package cn.iocoder.yudao.module.game.dal.dataobject.instance;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 机器信息 DO
 *
 * @author 管理员
 */
@TableName("game_instance")
@KeySequence("game_instance_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 是否已售, 0:未售, 1:已售
     */
    private Boolean sale;
    /**
     * 云厂商code
     */
    private String companyCode;
    /**
     * 购买方式/渠道
     */
    private String wayOfBuy;
    /**
     * 是否是我们自己的机器, 针对dst套餐1所设置
     */
    private Boolean isOwn;
    /**
     * 云厂商实例对应账户
     */
    private String resourceAccount;
    /**
     * 云厂商实例对应账户密码
     */
    private String resourceAccountPassword;
    /**
     * 公网ip
     */
    private String ip;
    /**
     * ssh 端口
     */
    private Integer sshPort;
    /**
     * ssh root密码
     */
    private String sshPassword;
    /**
     * 服务器所处地域
     */
    private String region;
    /**
     * 内存,单位G
     */
    private Double memory;
    /**
     * 核心数,单位G
     */
    private Integer cpu;
    /**
     * cpu频率
     */
    private Double cpuHz;
    /**
     * 磁盘容量
     */
    private Integer disk;
    /**
     * 磁盘容量剩余百分比
     */
    private Integer diskUsedPercent;
    /**
     * 所属套餐名称
     */
    private String packageName;
    /**
     * 服务器成本价格
     */
    private String price;
    /**
     * 购置服务器起租时间
     */
    private LocalDateTime startTime;
    /**
     * 购置服务器到期时间
     */
    private LocalDateTime endTime;
    /**
     * 用户承租到期时间
     */
    private LocalDateTime rentEndTime;
    /**
     * 通用agent最近一次启动时间
     */
    private LocalDateTime agentStartTime;
    /**
     * 通用agent最近一次心跳时间
     */
    private LocalDateTime agentHeartbeatTime;
    /**
     * 通用agent状态, up, down
     */
    private String agentStatus;
    /**
     * 云控agent端口
     */
    private Integer agentPort;
    /**
     * 云控agent websocket端口
     */
    private Integer agentWsPort;
    /**
     * agent version版本号
     */
    private String agentVersion;
    /**
     * 饥荒集群端口
     */
    private Integer portDstCluster;
    /**
     * 饥荒主世界端口
     */
    private Integer portDstMaster;
    /**
     * 饥荒洞穴端口
     */
    private Integer portDstCaves;
    /**
     * 七日杀世界端口
     */
    private Integer port7daydie;
    /**
     * 饥荒游戏版本，为空表示未安装该游戏
     */
    private String gameVersionDst;
    /**
     * 7日杀游戏版本，为空表示未安装该游戏
     */
    private String gameVersion7daydie;
    /**
     * 备注
     */
    private String notes;

}