package cn.iocoder.yudao.module.game.service.gamepackage;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.game.controller.admin.gamepackage.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.gamepackage.PackageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 饥荒套餐配置 Service 接口
 *
 * @author 管理员
 */
public interface PackageService {

    /**
     * 创建饥荒套餐配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPackage(@Valid PackageSaveReqVO createReqVO);

    /**
     * 更新饥荒套餐配置
     *
     * @param updateReqVO 更新信息
     */
    void updatePackage(@Valid PackageSaveReqVO updateReqVO);

    /**
     * 删除饥荒套餐配置
     *
     * @param id 编号
     */
    void deletePackage(Long id);

    /**
     * 获得饥荒套餐配置
     *
     * @param id 编号
     * @return 饥荒套餐配置
     */
    PackageDO getPackage(Long id);

    /**
     * 获得饥荒套餐配置分页
     *
     * @param pageReqVO 分页查询
     * @return 饥荒套餐配置分页
     */
    PageResult<PackageDO> getPackagePage(PackagePageReqVO pageReqVO);

}