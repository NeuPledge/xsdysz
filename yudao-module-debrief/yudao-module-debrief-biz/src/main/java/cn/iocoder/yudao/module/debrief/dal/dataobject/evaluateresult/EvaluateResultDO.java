package cn.iocoder.yudao.module.debrief.dal.dataobject.evaluateresult;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 评价结果 DO
 *
 * @author 芋道源码
 */
@TableName("debrief_evaluate_result")
@KeySequence("debrief_evaluate_result_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateResultDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 被评议党员id(debrief_party_member表的主键)
     */
    private Long partyMemberId;
    /**
     * 评议者id（debrief_student表的主键）
     */
    private Long commenterId;
    /**
     * 1.满意; 2.基本满意; 3.不满意
     */
    private Long comment;
    /**
     * 支部id
     */
    private Long branchId;
    /**
     * 学院id
     */
    private Long collegeId;
    /**
     * 班级id
     */
    private Long gradeId;
    /**
     * 其他评价内容
     */
    private String content;

}