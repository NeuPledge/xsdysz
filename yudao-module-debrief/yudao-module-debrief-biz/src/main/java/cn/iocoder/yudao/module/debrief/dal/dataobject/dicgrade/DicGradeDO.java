package cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 班级字典列 DO
 *
 * @author 芋道源码
 */
@TableName("debrief_dic_grade")
@KeySequence("debrief_dic_grade_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DicGradeDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 学院id
     */
    private Long collegeId;
    /**
     * 班级名
     */
    private String gradeName;

}