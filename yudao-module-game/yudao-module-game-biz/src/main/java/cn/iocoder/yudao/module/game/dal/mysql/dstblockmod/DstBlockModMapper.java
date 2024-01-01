package cn.iocoder.yudao.module.game.dal.mysql.dstblockmod;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.dstblockmod.DstBlockModDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.game.controller.admin.dstblockmod.vo.*;

/**
 * 模组黑名单,此名单上的模组,将会警告提示/禁止添加 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface DstBlockModMapper extends BaseMapperX<DstBlockModDO> {

    default PageResult<DstBlockModDO> selectPage(DstBlockModPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DstBlockModDO>()
                .eqIfPresent(DstBlockModDO::getModId, reqVO.getModId())
                .eqIfPresent(DstBlockModDO::getModTitle, reqVO.getModTitle())
                .eqIfPresent(DstBlockModDO::getDesc, reqVO.getDesc())
                .eqIfPresent(DstBlockModDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DstBlockModDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DstBlockModDO::getId));
    }

}