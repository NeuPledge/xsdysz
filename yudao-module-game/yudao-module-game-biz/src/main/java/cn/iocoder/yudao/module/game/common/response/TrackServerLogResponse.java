package cn.iocoder.yudao.module.game.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class TrackServerLogResponse {

    @Schema(description = "报错的日志")
    private List<String> segmentLogs;

    @Schema(description = "错误分析提示")
    private String errorTips;
}
