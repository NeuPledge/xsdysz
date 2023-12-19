package cn.iocoder.yudao.module.debrief.service.diccollege;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.debrief.controller.admin.diccollege.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.diccollege.DicCollegeMapper;
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
 * {@link DicCollegeServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(DicCollegeServiceImpl.class)
public class DicCollegeServiceImplTest extends BaseDbUnitTest {

    @Resource
    private DicCollegeServiceImpl dicCollegeService;

    @Resource
    private DicCollegeMapper dicCollegeMapper;

    @Test
    public void testCreateDicCollege_success() {
        // 准备参数
        DicCollegeSaveReqVO createReqVO = randomPojo(DicCollegeSaveReqVO.class).setId(null);

        // 调用
        Long dicCollegeId = dicCollegeService.createDicCollege(createReqVO);
        // 断言
        assertNotNull(dicCollegeId);
        // 校验记录的属性是否正确
        DicCollegeDO dicCollege = dicCollegeMapper.selectById(dicCollegeId);
        assertPojoEquals(createReqVO, dicCollege, "id");
    }

    @Test
    public void testUpdateDicCollege_success() {
        // mock 数据
        DicCollegeDO dbDicCollege = randomPojo(DicCollegeDO.class);
        dicCollegeMapper.insert(dbDicCollege);// @Sql: 先插入出一条存在的数据
        // 准备参数
        DicCollegeSaveReqVO updateReqVO = randomPojo(DicCollegeSaveReqVO.class, o -> {
            o.setId(dbDicCollege.getId()); // 设置更新的 ID
        });

        // 调用
        dicCollegeService.updateDicCollege(updateReqVO);
        // 校验是否更新正确
        DicCollegeDO dicCollege = dicCollegeMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, dicCollege);
    }

    @Test
    public void testUpdateDicCollege_notExists() {
        // 准备参数
        DicCollegeSaveReqVO updateReqVO = randomPojo(DicCollegeSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> dicCollegeService.updateDicCollege(updateReqVO), DIC_COLLEGE_NOT_EXISTS);
    }

    @Test
    public void testDeleteDicCollege_success() {
        // mock 数据
        DicCollegeDO dbDicCollege = randomPojo(DicCollegeDO.class);
        dicCollegeMapper.insert(dbDicCollege);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbDicCollege.getId();

        // 调用
        dicCollegeService.deleteDicCollege(id);
       // 校验数据不存在了
       assertNull(dicCollegeMapper.selectById(id));
    }

    @Test
    public void testDeleteDicCollege_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> dicCollegeService.deleteDicCollege(id), DIC_COLLEGE_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetDicCollegePage() {
       // mock 数据
       DicCollegeDO dbDicCollege = randomPojo(DicCollegeDO.class, o -> { // 等会查询到
           o.setCollegeName(null);
           o.setCreateTime(null);
       });
       dicCollegeMapper.insert(dbDicCollege);
       // 测试 collegeName 不匹配
       dicCollegeMapper.insert(cloneIgnoreId(dbDicCollege, o -> o.setCollegeName(null)));
       // 测试 createTime 不匹配
       dicCollegeMapper.insert(cloneIgnoreId(dbDicCollege, o -> o.setCreateTime(null)));
       // 准备参数
       DicCollegePageReqVO reqVO = new DicCollegePageReqVO();
       reqVO.setCollegeName(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<DicCollegeDO> pageResult = dicCollegeService.getDicCollegePage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbDicCollege, pageResult.getList().get(0));
    }

}