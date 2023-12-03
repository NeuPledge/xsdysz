package cn.iocoder.yudao.module.debrief.service.evaluateresult;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.evaluateresult.EvaluateResultDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 评价结果 Service 接口
 *
 * @author 芋道源码
 */
public interface EvaluateResultService {

    /**
     * 创建评价结果
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createEvaluateResult(@Valid EvaluateResultSaveReqVO createReqVO);

    /**
     * 更新评价结果
     *
     * @param updateReqVO 更新信息
     */
    void updateEvaluateResult(@Valid EvaluateResultSaveReqVO updateReqVO);

    /**
     * 删除评价结果
     *
     * @param id 编号
     */
    void deleteEvaluateResult(Long id);

    /**
     * 获得评价结果
     *
     * @param id 编号
     * @return 评价结果
     */
    EvaluateResultDO getEvaluateResult(Long id);

    /**
     * 获得评价结果分页
     *
     * @param pageReqVO 分页查询
     * @return 评价结果分页
     */
    PageResult<EvaluateResultDO> getEvaluateResultPage(EvaluateResultPageReqVO pageReqVO);

}