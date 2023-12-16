package cn.iocoder.yudao.module.debrief.controller.app.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
@ExcelIgnoreUnannotated
public class PartyMemberCommentData {
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

    @ExcelProperty("评价内容")
    private String content;
}
