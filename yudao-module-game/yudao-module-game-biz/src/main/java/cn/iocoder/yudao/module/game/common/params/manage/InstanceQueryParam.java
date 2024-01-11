package cn.iocoder.yudao.module.game.common.params.manage;

import cn.iocoder.yudao.module.game.common.params.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstanceQueryParam extends PageParam {

    private String ip;

    private Long instanceId;
    private Long userId;
    private Long rentId;

    private String region; // 地域

    private Long packageId; // 套餐ID

    private String companyCode; // 机器所属云厂商

    @Schema(description = "是否仅列出有效机器.")
    private Boolean valid;// 默认true,

    @Schema(description = "机器的状态")
    private String state; //

    @Schema(description = "是否显示今日已售的")
    private Boolean soldToday;

    @Schema(description = "指定游戏版本号(如果查询不等于该版本号,前面加!,比如   !533256   )")
    private String gameVersion;

    @Schema(description = "指定Agent版本号(如果查询不等于该版本号,前面加!,比如   !3.3.25.1   )")
    private String agentVersion;

    private String resourceAccount;

    public static class State {
        public static final String ALL = "All";
        public static final String IDLE = "Idle";
        public static final String SOLD = "Sold";

        public static final String UNSOLD = "Unsold";
        public static final String UNSOLD_7REVOKE = "Unsold7Revoke";
        public static final String UNSOLD_3REVOKE = "Unsold3Revoke";
        public static final String UNSOLD_1REVOKE = "Unsold1Revoke";
        public static final String BIND_RENT = "BindRent";
        public static final String VALID10DAY = "Valid10Day";
        public static final String VALID20DAY = "Valid20Day";
        public static final String VALID30DAY = "Valid30Day";

        public static final String DiskUsed50 = "DiskUsed50";// 还筛选硬盘使用超过50%
        public static final String DiskUsed80 = "DiskUsed80";// 还筛选硬盘使用超过80%

    }
}
