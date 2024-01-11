package cn.iocoder.yudao.module.game.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RentInfo {

    @Schema(description = "房间号")
    private Long roomNumber;

    @Schema(description = "人数上限")
    private Integer playerLimit;

    @Schema(description = "租约起始时间")
    private String rentStartTime;

    @Schema(description = "租约到期时间")
    private String rentEndTime;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "是否展示好评弹窗")
    private Boolean showHaoPingDialog;

    @Schema(description = "好评赠送时长. 0没有不赠送, 5:展示5天赠送时长, 3: 展示3天赠送时长.")
    private Integer haoPingDays = 0;

    @Schema(description = "存档名称")
    private String clusterName;

}
