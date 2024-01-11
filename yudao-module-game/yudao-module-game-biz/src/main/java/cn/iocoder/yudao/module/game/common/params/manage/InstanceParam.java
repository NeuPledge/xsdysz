package cn.iocoder.yudao.module.game.common.params.manage;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class InstanceParam {

    private Long id;

    private String companyCode;

    private String wayOfBuy;

    private Boolean isOwn;

    private String agentCode;

    private String resourceId;

    private String resourceAccount;

    private String resourceAccountPassword;

    private String hostname;

    private String ip;

    private Integer sshPort;

    private String sshPassword;

    private String innerIp;

    private String region;

    private Double memory;

    private Integer bandwidth;

    private Integer traffic;

    private Integer cpu;

    private Double cpuHz;

    private String packageName;

    private String price;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer dstAgentPort;

    private Integer dstAgentWsPort;

    private Integer dstClusterPort;

    private Integer dstMasterPort;

    private Integer dstCavesPort;

    private String notes;

}
