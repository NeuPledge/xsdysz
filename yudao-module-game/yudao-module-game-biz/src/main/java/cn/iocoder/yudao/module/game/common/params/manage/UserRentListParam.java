package cn.iocoder.yudao.module.game.common.params.manage;

import cn.iocoder.yudao.module.game.common.params.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRentListParam extends PageParam {

    private Long userId;
    private Long rentId;
    private Long instanceId;

    private String ip;

    @Schema(description = "是否筛选有效期内的租约. 如果选了startTime和endTime, 该值将失效.")
    private Boolean valid;// 默认true, 也可以传false, 可以查到历史租约的id.

    @Schema(description = "筛选出在租约过期时间在  startTime和endTime之间的租约.")
    private String startTime; // 一般是now()
    private String endTime;



}
