package cn.iocoder.yudao.module.game.dal.mysql.dstmods;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.dstmods.DstModsDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.game.controller.admin.dstmods.vo.*;

/**
 * 饥荒模组信息 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface DstModsMapper extends BaseMapperX<DstModsDO> {

    default PageResult<DstModsDO> selectPage(DstModsPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DstModsDO>()
                .eqIfPresent(DstModsDO::getModId, reqVO.getModId())
                .eqIfPresent(DstModsDO::getModTitle, reqVO.getModTitle())
                .eqIfPresent(DstModsDO::getIsServerMod, reqVO.getIsServerMod())
                .eqIfPresent(DstModsDO::getUrl, reqVO.getUrl())
                .eqIfPresent(DstModsDO::getTags, reqVO.getTags())
                .eqIfPresent(DstModsDO::getDependMods, reqVO.getDependMods())
                .eqIfPresent(DstModsDO::getImage, reqVO.getImage())
                .eqIfPresent(DstModsDO::getStarUrl, reqVO.getStarUrl())
                .eqIfPresent(DstModsDO::getAuthor, reqVO.getAuthor())
                .eqIfPresent(DstModsDO::getVersion, reqVO.getVersion())
                .eqIfPresent(DstModsDO::getFileSize, reqVO.getFileSize())
                .betweenIfPresent(DstModsDO::getModPublishTime, reqVO.getModPublishTime())
                .betweenIfPresent(DstModsDO::getModUpdateTime, reqVO.getModUpdateTime())
                .eqIfPresent(DstModsDO::getStar, reqVO.getStar())
                .eqIfPresent(DstModsDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DstModsDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DstModsDO::getId));
    }

}