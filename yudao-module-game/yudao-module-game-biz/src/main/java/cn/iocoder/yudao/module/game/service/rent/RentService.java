package cn.iocoder.yudao.module.game.service.rent;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.game.controller.admin.rent.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 租约 Service 接口
 *
 * @author 管理员
 */
public interface RentService {

    /**
     * 创建租约
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRent(@Valid RentSaveReqVO createReqVO);

    /**
     * 更新租约
     *
     * @param updateReqVO 更新信息
     */
    void updateRent(@Valid RentSaveReqVO updateReqVO);

    /**
     * 删除租约
     *
     * @param id 编号
     */
    void deleteRent(Long id);

    /**
     * 获得租约
     *
     * @param id 编号
     * @return 租约
     */
    RentDO getRent(Long id);

    /**
     * 获得租约分页
     *
     * @param pageReqVO 分页查询
     * @return 租约分页
     */
    PageResult<RentDO> getRentPage(RentPageReqVO pageReqVO);

}