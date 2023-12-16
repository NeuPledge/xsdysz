package cn.iocoder.yudao.module.debrief.controller.app.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class PartyMemberResultData {
    @ExcelProperty("党员数据库索引")
    private Long id;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("学号")
    private String studentNumber;

    @ExcelProperty("支部")
    private String branchName;

    @ExcelProperty("学院")
    private String collegeName;

    @ExcelProperty("班级")
    private String gradeName;

    @ExcelProperty("满意数")
    private Long satisfaction = 0L;

    @ExcelProperty("基本满意数")
    private Long baseSatisfaction = 0L;

    @ExcelProperty("不满意数")
    private Long dissatisfaction = 0L;

}
