package cn.iocoder.yudao.module.game.dal.mysql.idchost;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.idchost.IdcHostDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.game.controller.admin.idchost.vo.*;

/**
 * 机房主机 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface IdcHostMapper extends BaseMapperX<IdcHostDO> {

    default PageResult<IdcHostDO> selectPage(IdcHostPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<IdcHostDO>()
                .eqIfPresent(IdcHostDO::getIp, reqVO.getIp())
                .eqIfPresent(IdcHostDO::getCertPath, reqVO.getCertPath())
                .betweenIfPresent(IdcHostDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(IdcHostDO::getId));
    }

}