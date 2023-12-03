package cn.iocoder.yudao.module.debrief.controller.admin.dicbranch.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 支部字典列新增/修改 Request VO")
@Data
public class DicBranchSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "16069")
    private Long id;

    @Schema(description = "支部名", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "支部名不能为空")
    private String branchName;

}