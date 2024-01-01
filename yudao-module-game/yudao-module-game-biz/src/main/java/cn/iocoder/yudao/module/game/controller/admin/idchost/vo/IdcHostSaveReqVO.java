package cn.iocoder.yudao.module.game.controller.admin.idchost.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.util.*;

@Schema(description = "管理后台 - 机房主机新增/修改 Request VO")
@Data
public class IdcHostSaveReqVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "5533")
    private Long id;

    @Schema(description = "主机IP/域名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "主机IP/域名不能为空")
    private String ip;

    @Schema(description = "证书路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "证书路径不能为空")
    private String certPath;

}