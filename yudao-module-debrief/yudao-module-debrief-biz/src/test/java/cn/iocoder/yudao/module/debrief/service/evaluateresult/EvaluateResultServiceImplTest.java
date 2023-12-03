package cn.iocoder.yudao.module.debrief.service.evaluateresult;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.evaluateresult.EvaluateResultDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.evaluateresult.EvaluateResultMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.debrief.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link EvaluateResultServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(EvaluateResultServiceImpl.class)
public class EvaluateResultServiceImplTest extends BaseDbUnitTest {

    @Resource
    private EvaluateResultServiceImpl evaluateResultService;

    @Resource
    private EvaluateResultMapper evaluateResultMapper;

    @Test
    public void testCreateEvaluateResult_success() {
        // 准备参数
        EvaluateResultSaveReqVO createReqVO = randomPojo(EvaluateResultSaveReqVO.class).setId(null);

        // 调用
        Long evaluateResultId = evaluateResultService.createEvaluateResult(createReqVO);
        // 断言
        assertNotNull(evaluateResultId);
        // 校验记录的属性是否正确
        EvaluateResultDO evaluateResult = evaluateResultMapper.selectById(evaluateResultId);
        assertPojoEquals(createReqVO, evaluateResult, "id");
    }

    @Test
    public void testUpdateEvaluateResult_success() {
        // mock 数据
        EvaluateResultDO dbEvaluateResult = randomPojo(EvaluateResultDO.class);
        evaluateResultMapper.insert(dbEvaluateResult);// @Sql: 先插入出一条存在的数据
        // 准备参数
        EvaluateResultSaveReqVO updateReqVO = randomPojo(EvaluateResultSaveReqVO.class, o -> {
            o.setId(dbEvaluateResult.getId()); // 设置更新的 ID
        });

        // 调用
        evaluateResultService.updateEvaluateResult(updateReqVO);
        // 校验是否更新正确
        EvaluateResultDO evaluateResult = evaluateResultMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, evaluateResult);
    }

    @Test
    public void testUpdateEvaluateResult_notExists() {
        // 准备参数
        EvaluateResultSaveReqVO updateReqVO = randomPojo(EvaluateResultSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> evaluateResultService.updateEvaluateResult(updateReqVO), EVALUATE_RESULT_NOT_EXISTS);
    }

    @Test
    public void testDeleteEvaluateResult_success() {
        // mock 数据
        EvaluateResultDO dbEvaluateResult = randomPojo(EvaluateResultDO.class);
        evaluateResultMapper.insert(dbEvaluateResult);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbEvaluateResult.getId();

        // 调用
        evaluateResultService.deleteEvaluateResult(id);
       // 校验数据不存在了
       assertNull(evaluateResultMapper.selectById(id));
    }

    @Test
    public void testDeleteEvaluateResult_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> evaluateResultService.deleteEvaluateResult(id), EVALUATE_RESULT_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetEvaluateResultPage() {
       // mock 数据
       EvaluateResultDO dbEvaluateResult = randomPojo(EvaluateResultDO.class, o -> { // 等会查询到
           o.setPartyMemberId(null);
           o.setCommenterId(null);
           o.setComment(null);
           o.setBranchId(null);
           o.setCollegeId(null);
           o.setGradeId(null);
           o.setContent(null);
           o.setCreateTime(null);
       });
       evaluateResultMapper.insert(dbEvaluateResult);
       // 测试 partyMemberId 不匹配
       evaluateResultMapper.insert(cloneIgnoreId(dbEvaluateResult, o -> o.setPartyMemberId(null)));
       // 测试 commenterId 不匹配
       evaluateResultMapper.insert(cloneIgnoreId(dbEvaluateResult, o -> o.setCommenterId(null)));
       // 测试 comment 不匹配
       evaluateResultMapper.insert(cloneIgnoreId(dbEvaluateResult, o -> o.setComment(null)));
       // 测试 branchId 不匹配
       evaluateResultMapper.insert(cloneIgnoreId(dbEvaluateResult, o -> o.setBranchId(null)));
       // 测试 collegeId 不匹配
       evaluateResultMapper.insert(cloneIgnoreId(dbEvaluateResult, o -> o.setCollegeId(null)));
       // 测试 gradeId 不匹配
       evaluateResultMapper.insert(cloneIgnoreId(dbEvaluateResult, o -> o.setGradeId(null)));
       // 测试 content 不匹配
       evaluateResultMapper.insert(cloneIgnoreId(dbEvaluateResult, o -> o.setContent(null)));
       // 测试 createTime 不匹配
       evaluateResultMapper.insert(cloneIgnoreId(dbEvaluateResult, o -> o.setCreateTime(null)));
       // 准备参数
       EvaluateResultPageReqVO reqVO = new EvaluateResultPageReqVO();
       reqVO.setPartyMemberId(null);
       reqVO.setCommenterId(null);
       reqVO.setComment(null);
       reqVO.setBranchId(null);
       reqVO.setCollegeId(null);
       reqVO.setGradeId(null);
       reqVO.setContent(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<EvaluateResultDO> pageResult = evaluateResultService.getEvaluateResultPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbEvaluateResult, pageResult.getList().get(0));
    }

}