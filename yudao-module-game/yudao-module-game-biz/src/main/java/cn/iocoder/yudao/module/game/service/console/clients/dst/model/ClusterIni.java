package cn.iocoder.yudao.module.game.service.console.clients.dst.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterIni {

    private NETWORK NETWORK;
    private MISC MISC;
    private SHARD SHARD;
    private GAMEPLAY GAMEPLAY;
    private STEAM STEAM;

    // 群组服务器
    @Data
    public static class STEAM {
        private Boolean steam_group_admins;// true/false 自动组版主在游戏里获得管理员身份
        private String steam_group_id; // 群组id  42534338
        private Boolean steam_group_only; // true/false  为true表示只能群组内的群员可以加入.
    }


    @Data
    public static class NETWORK {

        private Boolean offline_cluster = false; // 必须false
        private String cluster_cloud_id; // 云id， 貌似重复也没关系
        private Boolean lan_only_cluster = false; // 必须false
        private String cluster_language;// 游戏语言
        // 用户可以自行修改的配置
        private String cluster_intention;// 游戏风格  cooperative合作/competitive竞争/social社交/madness疯狂
        private String cluster_description; // 房间描述
        private String cluster_name;// 房间名称
        private String cluster_password; // 房间密码
        private Integer whitelist_slots; // 为白名单用户保留的游戏位, 假设最大玩家数为6，whitelist_slots 里有2个玩家，所以实际上普通玩家的最大数量是4
    }

    @Data
    public static class MISC {

        private Boolean console_enabled = true; // 开启控制台超级命令
        private Integer max_snapshots = 10; // 可以回档的天数, 默认6(但是只会保存5份历史节点), 这里调为10
    }

    @Data
    public static class SHARD {

        private String bind_ip;
        private Boolean shard_enabled;
        private String cluster_key;
        private String master_port;
        private String master_ip;
    }

    @Data
    public static class GAMEPLAY {

        private Integer max_players;  //  最大人数上限, 用户自行设置, 但无法超过套餐上限.  [数值]
        private String game_mode; // 游戏模式   survival生存/endless无尽/wilderness荒野/lavaarena熔炉/quagmire暴食
        private Boolean pause_when_empty = true; // 大部分都是true
        private Boolean pvp; // 默认false， 用户也可以修改
    }

}
