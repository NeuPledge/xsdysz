package cn.iocoder.yudao.module.game.service.console.clients.dst.model;

import java.util.List;
import lombok.Data;

@Data
public class SteamModInfo {

    private String modId;
    private Integer isServerMod;// 非 client_only_mod 1   0:否,1:是
    private String tags;

    private Integer status;// 模组状态,0:不可用,1:可用

    private String title;
    private String filesize;
    private String modPublishTime;
    private String modUpdateTime;

    private String author;

    private String version;

    private String image;

    private String starUrl;

    private String url;

    // 依赖的模组Id(存数据库用)
    private List<String> dependMods;
    // 依赖的模组对象(前端显示用)
//    private List<SteamModInfo> dependModInfos;

}
