package cn.iocoder.yudao.module.game.dal.mysql.operatelog;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.operatelog.GameOperateLogDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.game.controller.admin.operatelog.vo.*;

/**
 * 操作日志 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface GameOperateLogMapper extends BaseMapperX<GameOperateLogDO> {

    default PageResult<GameOperateLogDO> selectPage(OperateLogPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<GameOperateLogDO>()
                .eqIfPresent(GameOperateLogDO::getGameCode, reqVO.getGameCode())
                .eqIfPresent(GameOperateLogDO::getUserId, reqVO.getUserId())
                .eqIfPresent(GameOperateLogDO::getRentId, reqVO.getRentId())
                .eqIfPresent(GameOperateLogDO::getInstanceId, reqVO.getInstanceId())
                .eqIfPresent(GameOperateLogDO::getInstanceIp, reqVO.getInstanceIp())
                .likeIfPresent(GameOperateLogDO::getName, reqVO.getName())
                .betweenIfPresent(GameOperateLogDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(GameOperateLogDO::getId));
    }

}