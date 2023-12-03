package cn.iocoder.yudao.module.debrief.controller.app.request;

import cn.iocoder.yudao.module.debrief.controller.app.request.vo.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class SubmitCommentRequest {

    @Schema(description = "评议提交者的id")
    private Long id;
    @Schema(description = "党员Id和评议选项")
    private List<Comment> comments;
}
