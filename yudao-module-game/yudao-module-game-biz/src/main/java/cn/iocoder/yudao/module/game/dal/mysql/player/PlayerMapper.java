package cn.iocoder.yudao.module.game.dal.mysql.player;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.player.PlayerDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.game.controller.admin.player.vo.*;

/**
 * 记录局内玩家人数 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface PlayerMapper extends BaseMapperX<PlayerDO> {

    default PageResult<PlayerDO> selectPage(PlayerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PlayerDO>()
                .eqIfPresent(PlayerDO::getGameCode, reqVO.getGameCode())
                .eqIfPresent(PlayerDO::getUserId, reqVO.getUserId())
                .eqIfPresent(PlayerDO::getRentId, reqVO.getRentId())
                .eqIfPresent(PlayerDO::getInstanceIp, reqVO.getInstanceIp())
                .eqIfPresent(PlayerDO::getNumber, reqVO.getNumber())
                .betweenIfPresent(PlayerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PlayerDO::getId));
    }

}