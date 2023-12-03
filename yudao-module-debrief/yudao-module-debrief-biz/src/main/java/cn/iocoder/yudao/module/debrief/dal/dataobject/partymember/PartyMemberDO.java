package cn.iocoder.yudao.module.debrief.dal.dataobject.partymember;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 党员信息 DO
 *
 * @author 芋道源码
 */
@TableName("debrief_party_member")
@KeySequence("debrief_party_member_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartyMemberDO extends BaseDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 学生id
     */
    private Long debriefStudentId;
    /**
     * 学生姓名
     */
    private String name;
    /**
     * 学号
     */
    private String studentNumber;
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
     * 满意数
     */
    private Long satisfaction;
    /**
     * 基本满意数
     */
    private Long baseSatisfaction;
    /**
     * 不满意数
     */
    private Long dissatisfaction;
    /**
     * 最后修改时间
     */
    private LocalDateTime modifyTime;

}