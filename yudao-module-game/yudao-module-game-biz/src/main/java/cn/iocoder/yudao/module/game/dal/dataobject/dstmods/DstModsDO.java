package cn.iocoder.yudao.module.game.dal.dataobject.dstmods;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 饥荒模组信息 DO
 *
 * @author 管理员
 */
@TableName("game_dst_mods")
@KeySequence("game_dst_mods_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DstModsDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;
    /**
     * 模组id(不含workshop-)
     */
    private Long modId;
    /**
     * 模组名称
     */
    private String modTitle;
    /**
     * 是否为服务端模组
     */
    private Integer isServerMod;
    /**
     * 模组steam链接
     */
    private String url;
    /**
     * 标签
     */
    private String tags;
    /**
     * 依赖的模组
     */
    private String dependMods;
    /**
     * 模组图标
     */
    private String image;
    /**
     * 模组评分星级图标
     */
    private String starUrl;
    /**
     * 作者
     */
    private String author;
    /**
     * 版本
     */
    private String version;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 模组发布时间
     */
    private String modPublishTime;
    /**
     * 模组更新时间
     */
    private String modUpdateTime;
    /**
     * 模组评分星级
     */
    private Integer star;
    /**
     * 模组状态,0:不可用,1:可用
     */
    private Integer status;

}