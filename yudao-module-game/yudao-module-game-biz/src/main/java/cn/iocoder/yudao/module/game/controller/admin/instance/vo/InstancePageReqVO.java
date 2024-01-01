package cn.iocoder.yudao.module.game.controller.admin.instance.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 机器信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InstancePageReqVO extends PageParam {

    @Schema(description = "是否已售, 0:未售, 1:已售")
    private Boolean sale;

    @Schema(description = "云厂商code")
    private String companyCode;

    @Schema(description = "购买方式/渠道")
    private String wayOfBuy;

    @Schema(description = "是否是我们自己的机器, 针对dst套餐1所设置")
    private Boolean isOwn;

    @Schema(description = "云厂商实例对应账户", example = "30115")
    private String resourceAccount;

    @Schema(description = "云厂商实例对应账户密码")
    private String resourceAccountPassword;

    @Schema(description = "公网ip")
    private String ip;

    @Schema(description = "ssh 端口")
    private Integer sshPort;

    @Schema(description = "ssh root密码")
    private String sshPassword;

    @Schema(description = "服务器所处地域")
    private String region;

    @Schema(description = "内存,单位G")
    private Double memory;

    @Schema(description = "核心数,单位G")
    private Integer cpu;

    @Schema(description = "cpu频率")
    private Double cpuHz;

    @Schema(description = "磁盘容量")
    private Integer disk;

    @Schema(description = "磁盘容量剩余百分比")
    private Integer diskUsedPercent;

    @Schema(description = "所属套餐名称", example = "芋艿")
    private String packageName;

    @Schema(description = "服务器成本价格", example = "7090")
    private String price;

    @Schema(description = "购置服务器起租时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "购置服务器到期时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] endTime;

    @Schema(description = "用户承租到期时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] rentEndTime;

    @Schema(description = "通用agent最近一次启动时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] agentStartTime;

    @Schema(description = "通用agent最近一次心跳时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] agentHeartbeatTime;

    @Schema(description = "通用agent状态, up, down", example = "2")
    private String agentStatus;

    @Schema(description = "云控agent端口")
    private Integer agentPort;

    @Schema(description = "云控agent websocket端口")
    private Integer agentWsPort;

    @Schema(description = "agent version版本号")
    private String agentVersion;

    @Schema(description = "饥荒集群端口")
    private Integer portDstCluster;

    @Schema(description = "饥荒主世界端口")
    private Integer portDstMaster;

    @Schema(description = "饥荒洞穴端口")
    private Integer portDstCaves;

    @Schema(description = "七日杀世界端口")
    private Integer port7daydie;

    @Schema(description = "饥荒游戏版本，为空表示未安装该游戏")
    private String gameVersionDst;

    @Schema(description = "7日杀游戏版本，为空表示未安装该游戏")
    private String gameVersion7daydie;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}