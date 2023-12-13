package cn.iocoder.yudao.module.debrief.service.dicgrade;

import cn.iocoder.yudao.module.debrief.controller.app.response.DicCollegeResponse;
import cn.iocoder.yudao.module.debrief.service.DicService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.debrief.controller.admin.dicgrade.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade.DicGradeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.debrief.dal.mysql.dicgrade.DicGradeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.debrief.enums.ErrorCodeConstants.*;

/**
 * 班级字典列 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DicGradeServiceImpl implements DicGradeService {

    @Resource
    private DicGradeMapper dicGradeMapper;

    @Resource
    private DicService dicService;

    @Override
    public Long createDicGrade(DicGradeSaveReqVO createReqVO) {
        // 插入
        DicGradeDO dicGrade = BeanUtils.toBean(createReqVO, DicGradeDO.class);
        dicGradeMapper.insert(dicGrade);
        // 返回
        return dicGrade.getId();
    }

    @Override
    public void updateDicGrade(DicGradeSaveReqVO updateReqVO) {
        // 校验存在
        validateDicGradeExists(updateReqVO.getId());
        // 更新
        DicGradeDO updateObj = BeanUtils.toBean(updateReqVO, DicGradeDO.class);
        dicGradeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDicGrade(Long id) {
        // 校验存在
        validateDicGradeExists(id);
        // 删除
        dicGradeMapper.deleteById(id);
    }

    private void validateDicGradeExists(Long id) {
        if (dicGradeMapper.selectById(id) == null) {
            throw exception(DIC_GRADE_NOT_EXISTS);
        }
    }

    @Override
    public DicGradeDO getDicGrade(Long id) {
        return dicGradeMapper.selectById(id);
    }

    @Override
    public PageResult<DicGradeDO> getDicGradePage(DicGradePageReqVO pageReqVO) {
        PageResult<DicGradeDO> dicGradeDOPageResult = dicGradeMapper.selectPage(pageReqVO);
        return dicGradeDOPageResult;
    }

}