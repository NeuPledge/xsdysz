package cn.iocoder.yudao.module.debrief.controller.admin.diccollege.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 学院字典列新增/修改 Request VO")
@Data
public class DicCollegeSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "11761")
    private Long id;

    @Schema(description = "学院名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "学院名不能为空")
    private String collegeName;

}