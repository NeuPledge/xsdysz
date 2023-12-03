package cn.iocoder.yudao.module.debrief.service.evaluateresult;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.evaluateresult.EvaluateResultDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.debrief.dal.mysql.evaluateresult.EvaluateResultMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.debrief.enums.ErrorCodeConstants.*;

/**
 * 评价结果 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class EvaluateResultServiceImpl implements EvaluateResultService {

    @Resource
    private EvaluateResultMapper evaluateResultMapper;

    @Override
    public Long createEvaluateResult(EvaluateResultSaveReqVO createReqVO) {
        // 插入
        EvaluateResultDO evaluateResult = BeanUtils.toBean(createReqVO, EvaluateResultDO.class);
        evaluateResultMapper.insert(evaluateResult);
        // 返回
        return evaluateResult.getId();
    }

    @Override
    public void updateEvaluateResult(EvaluateResultSaveReqVO updateReqVO) {
        // 校验存在
        validateEvaluateResultExists(updateReqVO.getId());
        // 更新
        EvaluateResultDO updateObj = BeanUtils.toBean(updateReqVO, EvaluateResultDO.class);
        evaluateResultMapper.updateById(updateObj);
    }

    @Override
    public void deleteEvaluateResult(Long id) {
        // 校验存在
        validateEvaluateResultExists(id);
        // 删除
        evaluateResultMapper.deleteById(id);
    }

    private void validateEvaluateResultExists(Long id) {
        if (evaluateResultMapper.selectById(id) == null) {
            throw exception(EVALUATE_RESULT_NOT_EXISTS);
        }
    }

    @Override
    public EvaluateResultDO getEvaluateResult(Long id) {
        return evaluateResultMapper.selectById(id);
    }

    @Override
    public PageResult<EvaluateResultDO> getEvaluateResultPage(EvaluateResultPageReqVO pageReqVO) {
        return evaluateResultMapper.selectPage(pageReqVO);
    }

}