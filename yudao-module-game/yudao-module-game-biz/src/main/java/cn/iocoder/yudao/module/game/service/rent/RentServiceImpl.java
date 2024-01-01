package cn.iocoder.yudao.module.game.service.rent;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.game.controller.admin.rent.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.rent.RentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.game.dal.mysql.rent.RentMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.game.enums.ErrorCodeConstants.*;

/**
 * 租约 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class RentServiceImpl implements RentService {

    @Resource
    private RentMapper rentMapper;

    @Override
    public Long createRent(RentSaveReqVO createReqVO) {
        // 插入
        RentDO rent = BeanUtils.toBean(createReqVO, RentDO.class);
        rentMapper.insert(rent);
        // 返回
        return rent.getId();
    }

    @Override
    public void updateRent(RentSaveReqVO updateReqVO) {
        // 校验存在
        validateRentExists(updateReqVO.getId());
        // 更新
        RentDO updateObj = BeanUtils.toBean(updateReqVO, RentDO.class);
        rentMapper.updateById(updateObj);
    }

    @Override
    public void deleteRent(Long id) {
        // 校验存在
        validateRentExists(id);
        // 删除
        rentMapper.deleteById(id);
    }

    private void validateRentExists(Long id) {
        if (rentMapper.selectById(id) == null) {
            throw exception(RENT_NOT_EXISTS);
        }
    }

    @Override
    public RentDO getRent(Long id) {
        return rentMapper.selectById(id);
    }

    @Override
    public PageResult<RentDO> getRentPage(RentPageReqVO pageReqVO) {
        return rentMapper.selectPage(pageReqVO);
    }

}