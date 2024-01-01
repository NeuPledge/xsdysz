package cn.iocoder.yudao.module.game.dal.mysql.instance;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.game.controller.admin.instance.vo.*;

/**
 * 机器信息 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface InstanceMapper extends BaseMapperX<InstanceDO> {

    default PageResult<InstanceDO> selectPage(InstancePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InstanceDO>()
                .eqIfPresent(InstanceDO::getSale, reqVO.getSale())
                .eqIfPresent(InstanceDO::getCompanyCode, reqVO.getCompanyCode())
                .eqIfPresent(InstanceDO::getWayOfBuy, reqVO.getWayOfBuy())
                .eqIfPresent(InstanceDO::getIsOwn, reqVO.getIsOwn())
                .eqIfPresent(InstanceDO::getResourceAccount, reqVO.getResourceAccount())
                .eqIfPresent(InstanceDO::getResourceAccountPassword, reqVO.getResourceAccountPassword())
                .eqIfPresent(InstanceDO::getIp, reqVO.getIp())
                .eqIfPresent(InstanceDO::getSshPort, reqVO.getSshPort())
                .eqIfPresent(InstanceDO::getSshPassword, reqVO.getSshPassword())
                .eqIfPresent(InstanceDO::getRegion, reqVO.getRegion())
                .eqIfPresent(InstanceDO::getMemory, reqVO.getMemory())
                .eqIfPresent(InstanceDO::getCpu, reqVO.getCpu())
                .eqIfPresent(InstanceDO::getCpuHz, reqVO.getCpuHz())
                .eqIfPresent(InstanceDO::getDisk, reqVO.getDisk())
                .eqIfPresent(InstanceDO::getDiskUsedPercent, reqVO.getDiskUsedPercent())
                .likeIfPresent(InstanceDO::getPackageName, reqVO.getPackageName())
                .eqIfPresent(InstanceDO::getPrice, reqVO.getPrice())
                .betweenIfPresent(InstanceDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(InstanceDO::getEndTime, reqVO.getEndTime())
                .betweenIfPresent(InstanceDO::getRentEndTime, reqVO.getRentEndTime())
                .betweenIfPresent(InstanceDO::getAgentStartTime, reqVO.getAgentStartTime())
                .betweenIfPresent(InstanceDO::getAgentHeartbeatTime, reqVO.getAgentHeartbeatTime())
                .eqIfPresent(InstanceDO::getAgentStatus, reqVO.getAgentStatus())
                .eqIfPresent(InstanceDO::getAgentPort, reqVO.getAgentPort())
                .eqIfPresent(InstanceDO::getAgentWsPort, reqVO.getAgentWsPort())
                .eqIfPresent(InstanceDO::getAgentVersion, reqVO.getAgentVersion())
                .eqIfPresent(InstanceDO::getPortDstCluster, reqVO.getPortDstCluster())
                .eqIfPresent(InstanceDO::getPortDstMaster, reqVO.getPortDstMaster())
                .eqIfPresent(InstanceDO::getPortDstCaves, reqVO.getPortDstCaves())
                .eqIfPresent(InstanceDO::getPort7daydie, reqVO.getPort7daydie())
                .eqIfPresent(InstanceDO::getGameVersionDst, reqVO.getGameVersionDst())
                .eqIfPresent(InstanceDO::getGameVersion7daydie, reqVO.getGameVersion7daydie())
                .eqIfPresent(InstanceDO::getNotes, reqVO.getNotes())
                .betweenIfPresent(InstanceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(InstanceDO::getId));
    }

}