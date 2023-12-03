package cn.iocoder.yudao.module.debrief.controller.admin.student.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false) // 设置 chain = false，避免用户导入有问题
public class StudentImportVo {

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("学号")
    private String studentNumber;

    @ExcelProperty("班级")
    private String gradeName;

    @ExcelProperty("学院")
    private String collegeName;

    @ExcelProperty("是否是党员")
    private String isPartyMember;// 是/否

    @ExcelProperty("支部名称")
    private String branchName;

}
