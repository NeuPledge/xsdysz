package cn.iocoder.yudao.module.game.service.instance;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.game.controller.admin.instance.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.instance.InstanceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 机器信息 Service 接口
 *
 * @author 管理员
 */
public interface InstanceService {

    /**
     * 创建机器信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createInstance(@Valid InstanceSaveReqVO createReqVO);

    /**
     * 更新机器信息
     *
     * @param updateReqVO 更新信息
     */
    void updateInstance(@Valid InstanceSaveReqVO updateReqVO);

    /**
     * 删除机器信息
     *
     * @param id 编号
     */
    void deleteInstance(Long id);

    /**
     * 获得机器信息
     *
     * @param id 编号
     * @return 机器信息
     */
    InstanceDO getInstance(Long id);

    /**
     * 获得机器信息分页
     *
     * @param pageReqVO 分页查询
     * @return 机器信息分页
     */
    PageResult<InstanceDO> getInstancePage(InstancePageReqVO pageReqVO);

}