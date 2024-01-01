package cn.iocoder.yudao.module.game.service.instance;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.game.controller.admin.instance.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.game.dal.mysql.instance.InstanceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.game.enums.ErrorCodeConstants.*;

/**
 * 机器信息 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class InstanceServiceImpl implements InstanceService {

    @Resource
    private InstanceMapper instanceMapper;

    @Override
    public Long createInstance(InstanceSaveReqVO createReqVO) {
        // 插入
        InstanceDO instance = BeanUtils.toBean(createReqVO, InstanceDO.class);
        instanceMapper.insert(instance);
        // 返回
        return instance.getId();
    }

    @Override
    public void updateInstance(InstanceSaveReqVO updateReqVO) {
        // 校验存在
        validateInstanceExists(updateReqVO.getId());
        // 更新
        InstanceDO updateObj = BeanUtils.toBean(updateReqVO, InstanceDO.class);
        instanceMapper.updateById(updateObj);
    }

    @Override
    public void deleteInstance(Long id) {
        // 校验存在
        validateInstanceExists(id);
        // 删除
        instanceMapper.deleteById(id);
    }

    private void validateInstanceExists(Long id) {
        if (instanceMapper.selectById(id) == null) {
            throw exception(INSTANCE_NOT_EXISTS);
        }
    }

    @Override
    public InstanceDO getInstance(Long id) {
        return instanceMapper.selectById(id);
    }

    @Override
    public PageResult<InstanceDO> getInstancePage(InstancePageReqVO pageReqVO) {
        return instanceMapper.selectPage(pageReqVO);
    }

}