package cn.iocoder.yudao.module.game.service.snapshot;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.game.controller.admin.snapshot.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.snapshot.SnapshotDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.game.dal.mysql.snapshot.SnapshotMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.game.enums.ErrorCodeConstants.*;

/**
 * 快照, 便于误操作恢复数据 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class SnapshotServiceImpl implements SnapshotService {

    @Resource
    private SnapshotMapper snapshotMapper;

    @Override
    public Long createSnapshot(SnapshotSaveReqVO createReqVO) {
        // 插入
        SnapshotDO snapshot = BeanUtils.toBean(createReqVO, SnapshotDO.class);
        snapshotMapper.insert(snapshot);
        // 返回
        return snapshot.getId();
    }

    @Override
    public void updateSnapshot(SnapshotSaveReqVO updateReqVO) {
        // 校验存在
        validateSnapshotExists(updateReqVO.getId());
        // 更新
        SnapshotDO updateObj = BeanUtils.toBean(updateReqVO, SnapshotDO.class);
        snapshotMapper.updateById(updateObj);
    }

    @Override
    public void deleteSnapshot(Long id) {
        // 校验存在
        validateSnapshotExists(id);
        // 删除
        snapshotMapper.deleteById(id);
    }

    private void validateSnapshotExists(Long id) {
        if (snapshotMapper.selectById(id) == null) {
            throw exception(SNAPSHOT_NOT_EXISTS);
        }
    }

    @Override
    public SnapshotDO getSnapshot(Long id) {
        return snapshotMapper.selectById(id);
    }

    @Override
    public PageResult<SnapshotDO> getSnapshotPage(SnapshotPageReqVO pageReqVO) {
        return snapshotMapper.selectPage(pageReqVO);
    }

}