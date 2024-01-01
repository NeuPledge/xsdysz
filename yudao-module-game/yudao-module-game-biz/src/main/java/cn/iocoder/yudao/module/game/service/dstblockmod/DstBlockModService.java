package cn.iocoder.yudao.module.game.service.dstblockmod;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.game.controller.admin.dstblockmod.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.dstblockmod.DstBlockModDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 模组黑名单,此名单上的模组,将会警告提示/禁止添加 Service 接口
 *
 * @author 管理员
 */
public interface DstBlockModService {

    /**
     * 创建模组黑名单,此名单上的模组,将会警告提示/禁止添加
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDstBlockMod(@Valid DstBlockModSaveReqVO createReqVO);

    /**
     * 更新模组黑名单,此名单上的模组,将会警告提示/禁止添加
     *
     * @param updateReqVO 更新信息
     */
    void updateDstBlockMod(@Valid DstBlockModSaveReqVO updateReqVO);

    /**
     * 删除模组黑名单,此名单上的模组,将会警告提示/禁止添加
     *
     * @param id 编号
     */
    void deleteDstBlockMod(Long id);

    /**
     * 获得模组黑名单,此名单上的模组,将会警告提示/禁止添加
     *
     * @param id 编号
     * @return 模组黑名单,此名单上的模组,将会警告提示/禁止添加
     */
    DstBlockModDO getDstBlockMod(Long id);

    /**
     * 获得模组黑名单,此名单上的模组,将会警告提示/禁止添加分页
     *
     * @param pageReqVO 分页查询
     * @return 模组黑名单,此名单上的模组,将会警告提示/禁止添加分页
     */
    PageResult<DstBlockModDO> getDstBlockModPage(DstBlockModPageReqVO pageReqVO);

}