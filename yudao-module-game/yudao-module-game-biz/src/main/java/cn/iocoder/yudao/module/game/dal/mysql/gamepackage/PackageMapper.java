package cn.iocoder.yudao.module.game.dal.mysql.gamepackage;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.gamepackage.PackageDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.game.controller.admin.gamepackage.vo.*;

/**
 * 饥荒套餐配置 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface PackageMapper extends BaseMapperX<PackageDO> {

    default PageResult<PackageDO> selectPage(PackagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PackageDO>()
                .eqIfPresent(PackageDO::getGameCode, reqVO.getGameCode())
                .eqIfPresent(PackageDO::getPackageVersion, reqVO.getPackageVersion())
                .eqIfPresent(PackageDO::getPackageId, reqVO.getPackageId())
                .likeIfPresent(PackageDO::getName, reqVO.getName())
                .eqIfPresent(PackageDO::getDesc, reqVO.getDesc())
                .eqIfPresent(PackageDO::getPrice, reqVO.getPrice())
                .eqIfPresent(PackageDO::getPlayer, reqVO.getPlayer())
                .eqIfPresent(PackageDO::getCpu, reqVO.getCpu())
                .eqIfPresent(PackageDO::getMemoryMax, reqVO.getMemoryMax())
                .eqIfPresent(PackageDO::getMemoryMin, reqVO.getMemoryMin())
                .eqIfPresent(PackageDO::getCpuHzMin, reqVO.getCpuHzMin())
                .eqIfPresent(PackageDO::getCpuHzMax, reqVO.getCpuHzMax())
                .betweenIfPresent(PackageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PackageDO::getId));
    }

}