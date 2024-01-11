package cn.iocoder.yudao.module.game.common.params.manage;

import lombok.Data;

import java.util.Date;

@Data
public class RentBundleParam {

    private Long id;

    private Long userId;

    private Long packageId;

    private String gameCode;

    private Integer playerLimit;

//    private Long roomType;

    private Date startTime;

    private Date endTime;

    private Date deleteTime;

    private Long instanceId;

    private String instanceIp;

    private Integer instancePort;

    private String clusterName;

    private Integer agentPort;

    private String notes;

}
