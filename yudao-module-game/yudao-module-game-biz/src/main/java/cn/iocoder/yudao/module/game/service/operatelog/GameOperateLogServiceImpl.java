package cn.iocoder.yudao.module.game.service.operatelog;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.game.controller.admin.operatelog.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.operatelog.GameOperateLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.game.dal.mysql.operatelog.GameOperateLogMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.game.enums.ErrorCodeConstants.*;

/**
 * 操作日志 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class GameOperateLogServiceImpl implements GameOperateLogService {

    @Resource
    private GameOperateLogMapper gameOperateLogMapper;

    @Override
    public Long createOperateLog(OperateLogSaveReqVO createReqVO) {
        // 插入
        GameOperateLogDO operateLog = BeanUtils.toBean(createReqVO, GameOperateLogDO.class);
        gameOperateLogMapper.insert(operateLog);
        // 返回
        return operateLog.getId();
    }

    @Override
    public void updateOperateLog(OperateLogSaveReqVO updateReqVO) {
        // 校验存在
        validateOperateLogExists(updateReqVO.getId());
        // 更新
        GameOperateLogDO updateObj = BeanUtils.toBean(updateReqVO, GameOperateLogDO.class);
        gameOperateLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteOperateLog(Long id) {
        // 校验存在
        validateOperateLogExists(id);
        // 删除
        gameOperateLogMapper.deleteById(id);
    }

    private void validateOperateLogExists(Long id) {
        if (gameOperateLogMapper.selectById(id) == null) {
            throw exception(OPERATE_LOG_NOT_EXISTS);
        }
    }

    @Override
    public GameOperateLogDO getOperateLog(Long id) {
        return gameOperateLogMapper.selectById(id);
    }

    @Override
    public PageResult<GameOperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO) {
        return gameOperateLogMapper.selectPage(pageReqVO);
    }

}