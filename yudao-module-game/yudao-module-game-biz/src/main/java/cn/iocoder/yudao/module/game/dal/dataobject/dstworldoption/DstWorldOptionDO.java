package cn.iocoder.yudao.module.game.dal.dataobject.dstworldoption;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 饥荒世界配置初始化数据 DO
 *
 * @author 管理员
 */
@TableName("game_dst_world_option")
@KeySequence("game_dst_world_option_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DstWorldOptionDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 世界类型master/caves
     */
    private String world;
    /**
     * 配置类型, 世界选项/世界生成
     */
    private String configType;
    /**
     * 分类(生物/资源/...)
     */
    private String resourceType;
    /**
     * 配置标题(human read)
     */
    private String label;
    /**
     * 是否是全局配置
     */
    private Integer globalConfig;
    /**
     * 启用
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 配置name(key)
     */
    private String keyName;
    /**
     * 图标
     */
    private String icon;
    /**
     * 可选项(json数组)
     */
    private String options;
    /**
     * 默认选项
     */
    private String defaultOption;
    /**
     * 推荐选项(同理可以添加其他类型的选项)
     */
    private String recommendOption;
    /**
     * 轻松模式下的选项
     */
    private String relaxedOption;
    /**
     * 无尽模式下的选项
     */
    private String endlessOption;
    /**
     * 生存模式下的选项
     */
    private String survivalOption;
    /**
     * 荒野模式下的选项
     */
    private String wildernessOption;
    /**
     * 暗无天日模式下的选项
     */
    private String lightsoutOption;

}