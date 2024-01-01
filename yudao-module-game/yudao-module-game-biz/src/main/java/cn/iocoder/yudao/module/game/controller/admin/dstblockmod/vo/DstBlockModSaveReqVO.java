package cn.iocoder.yudao.module.game.controller.admin.dstblockmod.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 模组黑名单,此名单上的模组,将会警告提示/禁止添加新增/修改 Request VO")
@Data
public class DstBlockModSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27738")
    private Long id;

    @Schema(description = "模组id(不含workshop-)", example = "20369")
    private Long modId;

    @Schema(description = "模组名称")
    private String modTitle;

    @Schema(description = "拉黑理由")
    private String desc;

    @Schema(description = "warn/deny", example = "1")
    private String status;

}