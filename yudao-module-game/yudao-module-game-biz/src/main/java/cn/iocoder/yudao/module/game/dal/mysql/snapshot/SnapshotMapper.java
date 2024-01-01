package cn.iocoder.yudao.module.game.dal.mysql.snapshot;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.game.dal.dataobject.snapshot.SnapshotDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.game.controller.admin.snapshot.vo.*;

/**
 * 快照, 便于误操作恢复数据 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface SnapshotMapper extends BaseMapperX<SnapshotDO> {

    default PageResult<SnapshotDO> selectPage(SnapshotPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SnapshotDO>()
                .eqIfPresent(SnapshotDO::getGameCode, reqVO.getGameCode())
                .eqIfPresent(SnapshotDO::getAction, reqVO.getAction())
                .eqIfPresent(SnapshotDO::getSnapshot, reqVO.getSnapshot())
                .betweenIfPresent(SnapshotDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SnapshotDO::getId));
    }

}