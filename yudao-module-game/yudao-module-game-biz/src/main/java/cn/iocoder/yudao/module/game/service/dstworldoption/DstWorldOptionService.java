package cn.iocoder.yudao.module.game.service.dstworldoption;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.game.controller.admin.dstworldoption.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.dstworldoption.DstWorldOptionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 饥荒世界配置初始化数据 Service 接口
 *
 * @author 管理员
 */
public interface DstWorldOptionService {

    /**
     * 创建饥荒世界配置初始化数据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDstWorldOption(@Valid DstWorldOptionSaveReqVO createReqVO);

    /**
     * 更新饥荒世界配置初始化数据
     *
     * @param updateReqVO 更新信息
     */
    void updateDstWorldOption(@Valid DstWorldOptionSaveReqVO updateReqVO);

    /**
     * 删除饥荒世界配置初始化数据
     *
     * @param id 编号
     */
    void deleteDstWorldOption(Long id);

    /**
     * 获得饥荒世界配置初始化数据
     *
     * @param id 编号
     * @return 饥荒世界配置初始化数据
     */
    DstWorldOptionDO getDstWorldOption(Long id);

    /**
     * 获得饥荒世界配置初始化数据分页
     *
     * @param pageReqVO 分页查询
     * @return 饥荒世界配置初始化数据分页
     */
    PageResult<DstWorldOptionDO> getDstWorldOptionPage(DstWorldOptionPageReqVO pageReqVO);

}