package cn.iocoder.yudao.module.game.service.gamepackage;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.game.controller.admin.gamepackage.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.gamepackage.PackageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.game.dal.mysql.gamepackage.PackageMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.game.enums.ErrorCodeConstants.*;

/**
 * 饥荒套餐配置 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class PackageServiceImpl implements PackageService {

    @Resource
    private PackageMapper packageMapper;

    @Override
    public Long createPackage(PackageSaveReqVO createReqVO) {
        // 插入
        PackageDO gamePackage = BeanUtils.toBean(createReqVO, PackageDO.class);
        packageMapper.insert(gamePackage);
        // 返回
        return gamePackage.getId();
    }

    @Override
    public void updatePackage(PackageSaveReqVO updateReqVO) {
        // 校验存在
        validatePackageExists(updateReqVO.getId());
        // 更新
        PackageDO updateObj = BeanUtils.toBean(updateReqVO, PackageDO.class);
        packageMapper.updateById(updateObj);
    }

    @Override
    public void deletePackage(Long id) {
        // 校验存在
        validatePackageExists(id);
        // 删除
        packageMapper.deleteById(id);
    }

    private void validatePackageExists(Long id) {
        if (packageMapper.selectById(id) == null) {
            throw exception(PACKAGE_NOT_EXISTS);
        }
    }

    @Override
    public PackageDO getPackage(Long id) {
        return packageMapper.selectById(id);
    }

    @Override
    public PageResult<PackageDO> getPackagePage(PackagePageReqVO pageReqVO) {
        return packageMapper.selectPage(pageReqVO);
    }

}