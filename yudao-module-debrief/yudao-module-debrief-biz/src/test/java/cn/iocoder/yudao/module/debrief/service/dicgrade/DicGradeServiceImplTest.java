package cn.iocoder.yudao.module.debrief.service.dicgrade;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.debrief.controller.admin.dicgrade.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade.DicGradeDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicgrade.DicGradeMapper;
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
 * {@link DicGradeServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(DicGradeServiceImpl.class)
public class DicGradeServiceImplTest extends BaseDbUnitTest {

    @Resource
    private DicGradeServiceImpl dicGradeService;

    @Resource
    private DicGradeMapper dicGradeMapper;

    @Test
    public void testCreateDicGrade_success() {
        // 准备参数
        DicGradeSaveReqVO createReqVO = randomPojo(DicGradeSaveReqVO.class).setId(null);

        // 调用
        Long dicGradeId = dicGradeService.createDicGrade(createReqVO);
        // 断言
        assertNotNull(dicGradeId);
        // 校验记录的属性是否正确
        DicGradeDO dicGrade = dicGradeMapper.selectById(dicGradeId);
        assertPojoEquals(createReqVO, dicGrade, "id");
    }

    @Test
    public void testUpdateDicGrade_success() {
        // mock 数据
        DicGradeDO dbDicGrade = randomPojo(DicGradeDO.class);
        dicGradeMapper.insert(dbDicGrade);// @Sql: 先插入出一条存在的数据
        // 准备参数
        DicGradeSaveReqVO updateReqVO = randomPojo(DicGradeSaveReqVO.class, o -> {
            o.setId(dbDicGrade.getId()); // 设置更新的 ID
        });

        // 调用
        dicGradeService.updateDicGrade(updateReqVO);
        // 校验是否更新正确
        DicGradeDO dicGrade = dicGradeMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, dicGrade);
    }

    @Test
    public void testUpdateDicGrade_notExists() {
        // 准备参数
        DicGradeSaveReqVO updateReqVO = randomPojo(DicGradeSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> dicGradeService.updateDicGrade(updateReqVO), DIC_GRADE_NOT_EXISTS);
    }

    @Test
    public void testDeleteDicGrade_success() {
        // mock 数据
        DicGradeDO dbDicGrade = randomPojo(DicGradeDO.class);
        dicGradeMapper.insert(dbDicGrade);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbDicGrade.getId();

        // 调用
        dicGradeService.deleteDicGrade(id);
       // 校验数据不存在了
       assertNull(dicGradeMapper.selectById(id));
    }

    @Test
    public void testDeleteDicGrade_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> dicGradeService.deleteDicGrade(id), DIC_GRADE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetDicGradePage() {
       // mock 数据
       DicGradeDO dbDicGrade = randomPojo(DicGradeDO.class, o -> { // 等会查询到
           o.setCollegeId(null);
           o.setGradeName(null);
           o.setCreateTime(null);
       });
       dicGradeMapper.insert(dbDicGrade);
       // 测试 collegeId 不匹配
       dicGradeMapper.insert(cloneIgnoreId(dbDicGrade, o -> o.setCollegeId(null)));
       // 测试 gradeName 不匹配
       dicGradeMapper.insert(cloneIgnoreId(dbDicGrade, o -> o.setGradeName(null)));
       // 测试 createTime 不匹配
       dicGradeMapper.insert(cloneIgnoreId(dbDicGrade, o -> o.setCreateTime(null)));
       // 准备参数
       DicGradePageReqVO reqVO = new DicGradePageReqVO();
       reqVO.setCollegeId(null);
       reqVO.setGradeName(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<DicGradeDO> pageResult = dicGradeService.getDicGradePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbDicGrade, pageResult.getList().get(0));
    }

}