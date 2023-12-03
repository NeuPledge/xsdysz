package cn.iocoder.yudao.module.debrief.controller.app.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CheckRequest {
    private Long gradeId;

    @Schema(description = "check选中的党员id")
    private List<Long> ids;
}
