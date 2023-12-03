package cn.iocoder.yudao.module.debrief.service.dicbranch;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.debrief.controller.admin.dicbranch.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicbranch.DicBranchDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.debrief.dal.mysql.dicbranch.DicBranchMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.debrief.enums.ErrorCodeConstants.*;

/**
 * 支部字典列 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DicBranchServiceImpl implements DicBranchService {

    @Resource
    private DicBranchMapper dicBranchMapper;

    @Override
    public Long createDicBranch(DicBranchSaveReqVO createReqVO) {
        // 插入
        DicBranchDO dicBranch = BeanUtils.toBean(createReqVO, DicBranchDO.class);
        dicBranchMapper.insert(dicBranch);
        // 返回
        return dicBranch.getId();
    }

    @Override
    public void updateDicBranch(DicBranchSaveReqVO updateReqVO) {
        // 校验存在
        validateDicBranchExists(updateReqVO.getId());
        // 更新
        DicBranchDO updateObj = BeanUtils.toBean(updateReqVO, DicBranchDO.class);
        dicBranchMapper.updateById(updateObj);
    }

    @Override
    public void deleteDicBranch(Long id) {
        // 校验存在
        validateDicBranchExists(id);
        // 删除
        dicBranchMapper.deleteById(id);
    }

    private void validateDicBranchExists(Long id) {
        if (dicBranchMapper.selectById(id) == null) {
            throw exception(DIC_BRANCH_NOT_EXISTS);
        }
    }

    @Override
    public DicBranchDO getDicBranch(Long id) {
        return dicBranchMapper.selectById(id);
    }

    @Override
    public PageResult<DicBranchDO> getDicBranchPage(DicBranchPageReqVO pageReqVO) {
        return dicBranchMapper.selectPage(pageReqVO);
    }

}