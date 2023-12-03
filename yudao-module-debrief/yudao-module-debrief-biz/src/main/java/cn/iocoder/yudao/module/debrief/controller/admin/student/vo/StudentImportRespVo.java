package cn.iocoder.yudao.module.debrief.controller.admin.student.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class StudentImportRespVo {

    @Schema(description = "[学生]原始文件数据条数")
    private Integer studentCountInFile;

    @Schema(description = "[学生]入库数据条数")
    private Integer studentCountInDb;

    @Schema(description = "[学生]未入库的学生信息")
    private List<StudentImportVo> studentNotImport;

    @Schema(description = "[党员]原始文件数据条数")
    private Integer partyMemberCountInFile;

    @Schema(description = "[党员]入库数据条数")
    private Integer partyMemberCountInDb;

    @Schema(description = "[党员]未入库的党员信息")
    private List<StudentImportVo> partyMemberNotImport;


}
