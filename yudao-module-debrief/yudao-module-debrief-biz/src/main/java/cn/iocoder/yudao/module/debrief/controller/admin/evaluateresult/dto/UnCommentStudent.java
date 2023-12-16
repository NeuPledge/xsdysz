package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 党员完成趋势")
@Data
@ExcelIgnoreUnannotated
public class UnCommentStudent {

    @ExcelProperty("ID")
    private Long id;
    @ExcelProperty("学生姓名")
    private String name;
    @ExcelProperty("学号")
    private String studentNumber;
    @ExcelProperty("班级名称")
    private String gradeName;
    @ExcelProperty("学院名称")
    private String collegeName;

}
