package cn.iocoder.yudao.module.game.service.dstmods;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.game.controller.admin.dstmods.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.dstmods.DstModsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 饥荒模组信息 Service 接口
 *
 * @author 管理员
 */
public interface DstModsService {

    /**
     * 创建饥荒模组信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDstMods(@Valid DstModsSaveReqVO createReqVO);

    /**
     * 更新饥荒模组信息
     *
     * @param updateReqVO 更新信息
     */
    void updateDstMods(@Valid DstModsSaveReqVO updateReqVO);

    /**
     * 删除饥荒模组信息
     *
     * @param id 编号
     */
    void deleteDstMods(Long id);

    /**
     * 获得饥荒模组信息
     *
     * @param id 编号
     * @return 饥荒模组信息
     */
    DstModsDO getDstMods(Long id);

    /**
     * 获得饥荒模组信息分页
     *
     * @param pageReqVO 分页查询
     * @return 饥荒模组信息分页
     */
    PageResult<DstModsDO> getDstModsPage(DstModsPageReqVO pageReqVO);

}