package cn.iocoder.yudao.module.game.service.dstmods;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.game.controller.admin.dstmods.vo.*;
import cn.iocoder.yudao.module.game.dal.dataobject.dstmods.DstModsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.game.dal.mysql.dstmods.DstModsMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.game.enums.ErrorCodeConstants.*;

/**
 * 饥荒模组信息 Service 实现类
 *
 * @author 管理员
 */
@Service
@Validated
public class DstModsServiceImpl implements DstModsService {

    @Resource
    private DstModsMapper dstModsMapper;

    @Override
    public Long createDstMods(DstModsSaveReqVO createReqVO) {
        // 插入
        DstModsDO dstMods = BeanUtils.toBean(createReqVO, DstModsDO.class);
        dstModsMapper.insert(dstMods);
        // 返回
        return dstMods.getId();
    }

    @Override
    public void updateDstMods(DstModsSaveReqVO updateReqVO) {
        // 校验存在
        validateDstModsExists(updateReqVO.getId());
        // 更新
        DstModsDO updateObj = BeanUtils.toBean(updateReqVO, DstModsDO.class);
        dstModsMapper.updateById(updateObj);
    }

    @Override
    public void deleteDstMods(Long id) {
        // 校验存在
        validateDstModsExists(id);
        // 删除
        dstModsMapper.deleteById(id);
    }

    private void validateDstModsExists(Long id) {
        if (dstModsMapper.selectById(id) == null) {
            throw exception(DST_MODS_NOT_EXISTS);
        }
    }

    @Override
    public DstModsDO getDstMods(Long id) {
        return dstModsMapper.selectById(id);
    }

    @Override
    public PageResult<DstModsDO> getDstModsPage(DstModsPageReqVO pageReqVO) {
        return dstModsMapper.selectPage(pageReqVO);
    }

}