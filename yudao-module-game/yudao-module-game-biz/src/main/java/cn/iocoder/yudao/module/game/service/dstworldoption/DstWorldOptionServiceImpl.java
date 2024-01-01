package cn.iocoder.yudao.module.game.service.dstworldoption;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.game.controller.admin.dstworldoption.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.dstworldoption.DstWorldOptionDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.game.dal.mysql.dstworldoption.DstWorldOptionMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.game.enums.ErrorCodeConstants.*;

/**
 * 饥荒世界配置初始化数据 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class DstWorldOptionServiceImpl implements DstWorldOptionService {

    @Resource
    private DstWorldOptionMapper dstWorldOptionMapper;

    @Override
    public Long createDstWorldOption(DstWorldOptionSaveReqVO createReqVO) {
        // 插入
        DstWorldOptionDO dstWorldOption = BeanUtils.toBean(createReqVO, DstWorldOptionDO.class);
        dstWorldOptionMapper.insert(dstWorldOption);
        // 返回
        return dstWorldOption.getId();
    }

    @Override
    public void updateDstWorldOption(DstWorldOptionSaveReqVO updateReqVO) {
        // 校验存在
        validateDstWorldOptionExists(updateReqVO.getId());
        // 更新
        DstWorldOptionDO updateObj = BeanUtils.toBean(updateReqVO, DstWorldOptionDO.class);
        dstWorldOptionMapper.updateById(updateObj);
    }

    @Override
    public void deleteDstWorldOption(Long id) {
        // 校验存在
        validateDstWorldOptionExists(id);
        // 删除
        dstWorldOptionMapper.deleteById(id);
    }

    private void validateDstWorldOptionExists(Long id) {
        if (dstWorldOptionMapper.selectById(id) == null) {
            throw exception(DST_WORLD_OPTION_NOT_EXISTS);
        }
    }

    @Override
    public DstWorldOptionDO getDstWorldOption(Long id) {
        return dstWorldOptionMapper.selectById(id);
    }

    @Override
    public PageResult<DstWorldOptionDO> getDstWorldOptionPage(DstWorldOptionPageReqVO pageReqVO) {
        return dstWorldOptionMapper.selectPage(pageReqVO);
    }

}