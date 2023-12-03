package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 评价结果新增/修改 Request VO")
@Data
public class EvaluateResultSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22297")
    private Long id;

    @Schema(description = "被评议党员id(debrief_party_member表的主键)", requiredMode = Schema.RequiredMode.REQUIRED, example = "17375")
    @NotNull(message = "被评议党员id(debrief_party_member表的主键)不能为空")
    private Long partyMemberId;

    @Schema(description = "评议者id（debrief_student表的主键）", requiredMode = Schema.RequiredMode.REQUIRED, example = "26557")
    @NotNull(message = "评议者id（debrief_student表的主键）不能为空")
    private Long commenterId;

    @Schema(description = "1.满意; 2.基本满意; 3.不满意", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "1.满意; 2.基本满意; 3.不满意不能为空")
    private Long comment;

    @Schema(description = "支部id", example = "3840")
    private Long branchId;

    @Schema(description = "学院id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12314")
    @NotNull(message = "学院id不能为空")
    private Long collegeId;

    @Schema(description = "班级id", requiredMode = Schema.RequiredMode.REQUIRED, example = "31662")
    @NotNull(message = "班级id不能为空")
    private Long gradeId;

    @Schema(description = "其他评价内容")
    private String content;

}