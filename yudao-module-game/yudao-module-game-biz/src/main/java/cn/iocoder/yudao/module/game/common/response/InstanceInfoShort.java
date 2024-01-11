package cn.iocoder.yudao.module.game.common.response;

import com.dooyo.dao.entity.MBTInstanceEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 用于绑定租约快速选择用户id的下拉选项.
 */
@Getter
@Setter
public class InstanceInfoShort extends MBTInstanceEntity {

    @Schema(description = "是否推荐到当前套餐选项. 根据已选的套餐, 判断内存,带宽等参数是否匹配")
    private Boolean isPackageRecommend;

    @Schema(description = "是否已经绑定为测试设备")
    private Boolean isBindForTest;
}
