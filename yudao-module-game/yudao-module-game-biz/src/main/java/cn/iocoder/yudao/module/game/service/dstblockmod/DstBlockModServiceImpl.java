package cn.iocoder.yudao.module.game.service.dstblockmod;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.game.controller.admin.dstblockmod.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.dstblockmod.DstBlockModDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.game.dal.mysql.dstblockmod.DstBlockModMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.game.enums.ErrorCodeConstants.*;

/**
 * 模组黑名单,此名单上的模组,将会警告提示/禁止添加 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class DstBlockModServiceImpl implements DstBlockModService {

    @Resource
    private DstBlockModMapper dstBlockModMapper;

    @Override
    public Long createDstBlockMod(DstBlockModSaveReqVO createReqVO) {
        // 插入
        DstBlockModDO dstBlockMod = BeanUtils.toBean(createReqVO, DstBlockModDO.class);
        dstBlockModMapper.insert(dstBlockMod);
        // 返回
        return dstBlockMod.getId();
    }

    @Override
    public void updateDstBlockMod(DstBlockModSaveReqVO updateReqVO) {
        // 校验存在
        validateDstBlockModExists(updateReqVO.getId());
        // 更新
        DstBlockModDO updateObj = BeanUtils.toBean(updateReqVO, DstBlockModDO.class);
        dstBlockModMapper.updateById(updateObj);
    }

    @Override
    public void deleteDstBlockMod(Long id) {
        // 校验存在
        validateDstBlockModExists(id);
        // 删除
        dstBlockModMapper.deleteById(id);
    }

    private void validateDstBlockModExists(Long id) {
        if (dstBlockModMapper.selectById(id) == null) {
            throw exception(DST_BLOCK_MOD_NOT_EXISTS);
        }
    }

    @Override
    public DstBlockModDO getDstBlockMod(Long id) {
        return dstBlockModMapper.selectById(id);
    }

    @Override
    public PageResult<DstBlockModDO> getDstBlockModPage(DstBlockModPageReqVO pageReqVO) {
        return dstBlockModMapper.selectPage(pageReqVO);
    }

}