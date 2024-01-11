package cn.iocoder.yudao.module.game.common.response;

import lombok.Data;

@Data
public class AgentInfo {

    private String httpUrl;
    private Integer httpPort;
    private String wsUrl;// ws://101.89.205.227:18189/socket
    private Integer wsPort; //

}
