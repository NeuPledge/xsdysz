package cn.iocoder.yudao.module.game.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ClusterConfig {

    @Schema(description = "房间名称")
    private String roomName;

//    @Schema(description = "游戏风格")
//    private String gameIntention;
//
//    @Schema(description = "游戏风格")
//    private String gameIntentionStr;

    @Schema(description = "游戏模式")
    private String gameMode;

    @Schema(description = "游戏模式")
    private String gameModeStr;

    @Schema(description = "房间密码")
    private String roomPassword;

    @Schema(description = "房间描述")
    private String roomDescription;

    @Schema(description = "最大玩家人数")
    private Integer maxPlayers;

    @Schema(description = "套餐限制的最大玩家人数上限")
    private Integer maxPlayersLimit;

    @Schema(description = "玩家对战")
    private Boolean pvp;

    @Schema(description = "默认true, 游戏无玩家时是否自动暂停, 建议默认暂停")
    private Boolean pauseWhenEmpty;

    @Schema(description = "白名单槽位,默认0")
    private Integer whitelistSlots;

    @Schema(description = "是否启用群组服务器")
    private Boolean steamGroupEnabled;

    @Schema(description = "Steam群组ID")
    private String steamGroupId;

    @Schema(description = "是否只允许群组成员加入")
    private Boolean steamGroupOnly;

    @Schema(description = "组版主在游戏里是否自动获得管理员身份")
    private Boolean steamGroupAdmins;

    @Schema(description = "是否仅局域网玩家")
    private Boolean lanOnlyCluster;

    @Schema(description = "端口")
    private Integer clusterPort;

}
