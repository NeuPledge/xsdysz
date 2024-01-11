package cn.iocoder.yudao.module.game.common.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PageParam {

    // 分页排序
    @Schema(name = "页号")
    Integer pageNum;
    @Schema(name = "页大小")
    Integer pageSize;
    @Schema(name = "排序字段")
    String sortBy;
    @Schema(name = "排序顺序 asc/desc")
    String order;

}
