package cn.iocoder.yudao.module.debrief.dal.dataobject.student;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 学生信息 DO
 *
 * @author 芋道源码
 */
@TableName("debrief_student")
@KeySequence("debrief_student_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 学生姓名
     */
    private String name;
    /**
     * 学号
     */
    private String studentNumber;
    /**
     * 班级id
     */
    private Long gradeId;
    /**
     * 班级
     */
    private String gradeName;
    /**
     * 学院id
     */
    private Long collegeId;

}