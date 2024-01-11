package cn.iocoder.yudao.module.game.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {

    private Long id;
    private String username;
    private String passwordShow;
    private String phone;
    private String notes;


    @Schema(description = "用户购买之后发送快捷消息")
    private String buyText;
    @Schema(description = "为用户分配好服务器资源之后发送的快捷消息")
    private String assignText;
    @Schema(description = "为用户重置密码之后发送的快捷消息")
    private String resetPasswordText;

    private String role;
    @Schema(description = "是否已售")
    private Boolean sale;
    private String gameCode;
    private Integer playerLimit;
    private Long instanceId;
    private String instanceIp;
    @Schema(description = "游戏对应端口, 饥荒就是master的udp端口")
    private Integer instancePort;
    @Schema(description = "饥荒存档世界名称(文件夹名称)")
    private String clusterName;
    @Schema(description = "云控Agent的http端口")
    private Integer agentPort;

    private Long rentId;

    private Boolean blackUser;

    private Date startTime;
    private Date endTime;
    private Date deleteTime;
    private Date userCreateTime;
    private Date userUpdateTime;
    private Date rentCreateTime;
    private Date rentUpdateTime;
}
