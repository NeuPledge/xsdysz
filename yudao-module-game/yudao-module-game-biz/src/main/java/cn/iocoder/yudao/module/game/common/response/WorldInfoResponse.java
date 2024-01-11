package cn.iocoder.yudao.module.game.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WorldInfoResponse {

    @Schema(description = "房间名称")
    private String roomName;

    @Schema(description = "游戏风格")
    private String gameIntention;

    @Schema(description = "游戏模式")
    private String gameMode;

    @Schema(description = "已经过多少天")
    private int cycles;

    @Schema(description = "游戏天数")
    String currentDay;    // 第13+1天

    String phase;     // day/dusk/night

    String phase_zh;  // 白天

    String phaseRate; // 第14天已过20%

    @Schema(description = "季节")
    private String season;

//    @Schema(description = "距离月圆")
//    private int mooomphasecycle;

//    @Schema(description = "季节已进行天数")
//    private int elapseddaysinseason;

//    @Schema(description = "距离下个季节天数")
//    private int remainingdaysinseason;

    @Schema(description = "云服直连")
    private String consoleConnect;

    private String roomPassword;

    @Schema(description = "服务器版本号")
    private String version;

    @Schema(description = "是否开启洞穴, 0:未开启洞穴, 1:正常开启洞穴, -1:特价版禁止开洞穴")
    private Integer shardEnabled = 1;

}
