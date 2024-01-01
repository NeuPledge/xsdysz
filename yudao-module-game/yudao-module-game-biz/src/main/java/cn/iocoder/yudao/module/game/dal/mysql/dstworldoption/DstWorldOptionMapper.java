package cn.iocoder.yudao.module.game.dal.mysql.dstworldoption;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.dstworldoption.DstWorldOptionDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.game.controller.admin.dstworldoption.vo.*;

/**
 * 饥荒世界配置初始化数据 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface DstWorldOptionMapper extends BaseMapperX<DstWorldOptionDO> {

    default PageResult<DstWorldOptionDO> selectPage(DstWorldOptionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DstWorldOptionDO>()
                .eqIfPresent(DstWorldOptionDO::getWorld, reqVO.getWorld())
                .eqIfPresent(DstWorldOptionDO::getConfigType, reqVO.getConfigType())
                .eqIfPresent(DstWorldOptionDO::getResourceType, reqVO.getResourceType())
                .eqIfPresent(DstWorldOptionDO::getLabel, reqVO.getLabel())
                .eqIfPresent(DstWorldOptionDO::getGlobalConfig, reqVO.getGlobalConfig())
                .eqIfPresent(DstWorldOptionDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DstWorldOptionDO::getSort, reqVO.getSort())
                .likeIfPresent(DstWorldOptionDO::getKeyName, reqVO.getKeyName())
                .eqIfPresent(DstWorldOptionDO::getIcon, reqVO.getIcon())
                .eqIfPresent(DstWorldOptionDO::getOptions, reqVO.getOptions())
                .eqIfPresent(DstWorldOptionDO::getDefaultOption, reqVO.getDefaultOption())
                .eqIfPresent(DstWorldOptionDO::getRecommendOption, reqVO.getRecommendOption())
                .eqIfPresent(DstWorldOptionDO::getRelaxedOption, reqVO.getRelaxedOption())
                .eqIfPresent(DstWorldOptionDO::getEndlessOption, reqVO.getEndlessOption())
                .eqIfPresent(DstWorldOptionDO::getSurvivalOption, reqVO.getSurvivalOption())
                .eqIfPresent(DstWorldOptionDO::getWildernessOption, reqVO.getWildernessOption())
                .eqIfPresent(DstWorldOptionDO::getLightsoutOption, reqVO.getLightsoutOption())
                .betweenIfPresent(DstWorldOptionDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DstWorldOptionDO::getId));
    }

}