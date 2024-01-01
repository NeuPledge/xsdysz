package cn.iocoder.yudao.module.game.service.snapshot;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.game.controller.admin.snapshot.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.snapshot.SnapshotDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 快照, 便于误操作恢复数据 Service 接口
 *
 * @author 管理员
 */
public interface SnapshotService {

    /**
     * 创建快照, 便于误操作恢复数据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSnapshot(@Valid SnapshotSaveReqVO createReqVO);

    /**
     * 更新快照, 便于误操作恢复数据
     *
     * @param updateReqVO 更新信息
     */
    void updateSnapshot(@Valid SnapshotSaveReqVO updateReqVO);

    /**
     * 删除快照, 便于误操作恢复数据
     *
     * @param id 编号
     */
    void deleteSnapshot(Long id);

    /**
     * 获得快照, 便于误操作恢复数据
     *
     * @param id 编号
     * @return 快照, 便于误操作恢复数据
     */
    SnapshotDO getSnapshot(Long id);

    /**
     * 获得快照, 便于误操作恢复数据分页
     *
     * @param pageReqVO 分页查询
     * @return 快照, 便于误操作恢复数据分页
     */
    PageResult<SnapshotDO> getSnapshotPage(SnapshotPageReqVO pageReqVO);

}