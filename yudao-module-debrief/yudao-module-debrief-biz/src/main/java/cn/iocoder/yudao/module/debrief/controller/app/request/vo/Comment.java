package cn.iocoder.yudao.module.debrief.controller.app.request.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Comment {

    @Schema(description = "被评党员的id")
    private Long id;

    @Schema(description = "评议选项，1.满意；2.基本满意；3.不满意")
    private Long commentTypeId;

    @Schema(description = "评价的其他内容，是可选的")
    private String content;
}
