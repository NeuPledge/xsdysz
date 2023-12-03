package cn.iocoder.yudao.module.debrief.controller.admin.dicgrade.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 班级字典列新增/修改 Request VO")
@Data
public class DicGradeSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "28378")
    private Long id;

    @Schema(description = "学院id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23185")
    @NotNull(message = "学院id不能为空")
    private Long collegeId;

    @Schema(description = "班级名", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "班级名不能为空")
    private String gradeName;

}