package cn.iocoder.yudao.module.debrief.service.dicbranch;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.debrief.controller.admin.dicbranch.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicbranch.DicBranchDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicbranch.DicBranchMapper;
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
 * {@link DicBranchServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(DicBranchServiceImpl.class)
public class DicBranchServiceImplTest extends BaseDbUnitTest {

    @Resource
    private DicBranchServiceImpl dicBranchService;

    @Resource
    private DicBranchMapper dicBranchMapper;

    @Test
    public void testCreateDicBranch_success() {
        // 准备参数
        DicBranchSaveReqVO createReqVO = randomPojo(DicBranchSaveReqVO.class).setId(null);

        // 调用
        Long dicBranchId = dicBranchService.createDicBranch(createReqVO);
        // 断言
        assertNotNull(dicBranchId);
        // 校验记录的属性是否正确
        DicBranchDO dicBranch = dicBranchMapper.selectById(dicBranchId);
        assertPojoEquals(createReqVO, dicBranch, "id");
    }

    @Test
    public void testUpdateDicBranch_success() {
        // mock 数据
        DicBranchDO dbDicBranch = randomPojo(DicBranchDO.class);
        dicBranchMapper.insert(dbDicBranch);// @Sql: 先插入出一条存在的数据
        // 准备参数
        DicBranchSaveReqVO updateReqVO = randomPojo(DicBranchSaveReqVO.class, o -> {
            o.setId(dbDicBranch.getId()); // 设置更新的 ID
        });

        // 调用
        dicBranchService.updateDicBranch(updateReqVO);
        // 校验是否更新正确
        DicBranchDO dicBranch = dicBranchMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, dicBranch);
    }

    @Test
    public void testUpdateDicBranch_notExists() {
        // 准备参数
        DicBranchSaveReqVO updateReqVO = randomPojo(DicBranchSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> dicBranchService.updateDicBranch(updateReqVO), DIC_BRANCH_NOT_EXISTS);
    }

    @Test
    public void testDeleteDicBranch_success() {
        // mock 数据
        DicBranchDO dbDicBranch = randomPojo(DicBranchDO.class);
        dicBranchMapper.insert(dbDicBranch);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbDicBranch.getId();

        // 调用
        dicBranchService.deleteDicBranch(id);
       // 校验数据不存在了
       assertNull(dicBranchMapper.selectById(id));
    }

    @Test
    public void testDeleteDicBranch_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> dicBranchService.deleteDicBranch(id), DIC_BRANCH_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetDicBranchPage() {
       // mock 数据
       DicBranchDO dbDicBranch = randomPojo(DicBranchDO.class, o -> { // 等会查询到
           o.setBranchName(null);
           o.setCreateTime(null);
       });
       dicBranchMapper.insert(dbDicBranch);
       // 测试 branchName 不匹配
       dicBranchMapper.insert(cloneIgnoreId(dbDicBranch, o -> o.setBranchName(null)));
       // 测试 createTime 不匹配
       dicBranchMapper.insert(cloneIgnoreId(dbDicBranch, o -> o.setCreateTime(null)));
       // 准备参数
       DicBranchPageReqVO reqVO = new DicBranchPageReqVO();
       reqVO.setBranchName(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<DicBranchDO> pageResult = dicBranchService.getDicBranchPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbDicBranch, pageResult.getList().get(0));
    }

}