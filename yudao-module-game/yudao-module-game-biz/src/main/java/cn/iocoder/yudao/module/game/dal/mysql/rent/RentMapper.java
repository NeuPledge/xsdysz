package cn.iocoder.yudao.module.game.dal.mysql.rent;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.game.controller.admin.rent.vo.*;

/**
 * 租约 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface RentMapper extends BaseMapperX<RentDO> {

    default PageResult<RentDO> selectPage(RentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RentDO>()
                .eqIfPresent(RentDO::getUserId, reqVO.getUserId())
                .eqIfPresent(RentDO::getSale, reqVO.getSale())
                .eqIfPresent(RentDO::getPackageId, reqVO.getPackageId())
                .eqIfPresent(RentDO::getGameCode, reqVO.getGameCode())
                .eqIfPresent(RentDO::getPlayerLimit, reqVO.getPlayerLimit())
                .betweenIfPresent(RentDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(RentDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(RentDO::getInstanceId, reqVO.getInstanceId())
                .eqIfPresent(RentDO::getInstanceIp, reqVO.getInstanceIp())
                .likeIfPresent(RentDO::getClusterName, reqVO.getClusterName())
                .eqIfPresent(RentDO::getNotes, reqVO.getNotes())
                .eqIfPresent(RentDO::getAutoRenewal, reqVO.getAutoRenewal())
                .betweenIfPresent(RentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RentDO::getId));
    }

}