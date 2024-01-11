package cn.iocoder.yudao.module.game.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WorldOptionWithMode {
    @Schema(description = "frograin")
    private String key;
    @Schema(description = "青蛙雨")
    private String label;
    @Schema(description = "never")
    private String optionKey;
    @Schema(description = "无")
    private String optionLabel;

    @Schema(description = "属于该模式独有的, 这个字段挺难理解的.")
    private Boolean specialMode;

}
