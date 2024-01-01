package cn.iocoder.yudao.module.game.service.operatelog;

import javax.validation.*;
import cn.iocoder.yudao.module.game.controller.admin.operatelog.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.operatelog.GameOperateLogDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 操作日志 Service 接口
 *
 * @author 管理员
 */
public interface GameOperateLogService {

    /**
     * 创建操作日志
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createOperateLog(@Valid OperateLogSaveReqVO createReqVO);

    /**
     * 更新操作日志
     *
     * @param updateReqVO 更新信息
     */
    void updateOperateLog(@Valid OperateLogSaveReqVO updateReqVO);

    /**
     * 删除操作日志
     *
     * @param id 编号
     */
    void deleteOperateLog(Long id);

    /**
     * 获得操作日志
     *
     * @param id 编号
     * @return 操作日志
     */
    GameOperateLogDO getOperateLog(Long id);

    /**
     * 获得操作日志分页
     *
     * @param pageReqVO 分页查询
     * @return 操作日志分页
     */
    PageResult<GameOperateLogDO> getOperateLogPage(OperateLogPageReqVO pageReqVO);

}