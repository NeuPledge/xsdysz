package cn.iocoder.yudao.module.game.controller.admin.idchost.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 机房主机 Response VO")
@Data
@ExcelIgnoreUnannotated
public class IdcHostRespVO {

    @Schema(description = "自增主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "5533")
    @ExcelProperty("自增主键")
    private Long id;

    @Schema(description = "主机IP/域名", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("主机IP/域名")
    private String ip;

    @Schema(description = "证书路径", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("证书路径")
    private String certPath;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}