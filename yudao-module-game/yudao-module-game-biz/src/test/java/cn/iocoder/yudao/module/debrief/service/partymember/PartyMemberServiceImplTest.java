package cn.iocoder.yudao.module.debrief.service.partymember;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.debrief.controller.admin.partymember.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.partymember.PartyMemberDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.partymember.PartyMemberMapper;
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
 * {@link PartyMemberServiceImpl} 的单元测试类
 *
 * @author 芋道源码
 */
@Import(PartyMemberServiceImpl.class)
public class PartyMemberServiceImplTest extends BaseDbUnitTest {

    @Resource
    private PartyMemberServiceImpl partyMemberService;

    @Resource
    private PartyMemberMapper partyMemberMapper;

    @Test
    public void testCreatePartyMember_success() {
        // 准备参数
        PartyMemberSaveReqVO createReqVO = randomPojo(PartyMemberSaveReqVO.class).setId(null);

        // 调用
        Long partyMemberId = partyMemberService.createPartyMember(createReqVO);
        // 断言
        assertNotNull(partyMemberId);
        // 校验记录的属性是否正确
        PartyMemberDO partyMember = partyMemberMapper.selectById(partyMemberId);
        assertPojoEquals(createReqVO, partyMember, "id");
    }

    @Test
    public void testUpdatePartyMember_success() {
        // mock 数据
        PartyMemberDO dbPartyMember = randomPojo(PartyMemberDO.class);
        partyMemberMapper.insert(dbPartyMember);// @Sql: 先插入出一条存在的数据
        // 准备参数
        PartyMemberSaveReqVO updateReqVO = randomPojo(PartyMemberSaveReqVO.class, o -> {
            o.setId(dbPartyMember.getId()); // 设置更新的 ID
        });

        // 调用
        partyMemberService.updatePartyMember(updateReqVO);
        // 校验是否更新正确
        PartyMemberDO partyMember = partyMemberMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, partyMember);
    }

    @Test
    public void testUpdatePartyMember_notExists() {
        // 准备参数
        PartyMemberSaveReqVO updateReqVO = randomPojo(PartyMemberSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> partyMemberService.updatePartyMember(updateReqVO), PARTY_MEMBER_NOT_EXISTS);
    }

    @Test
    public void testDeletePartyMember_success() {
        // mock 数据
        PartyMemberDO dbPartyMember = randomPojo(PartyMemberDO.class);
        partyMemberMapper.insert(dbPartyMember);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbPartyMember.getId();

        // 调用
        partyMemberService.deletePartyMember(id);
       // 校验数据不存在了
       assertNull(partyMemberMapper.selectById(id));
    }

    @Test
    public void testDeletePartyMember_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> partyMemberService.deletePartyMember(id), PARTY_MEMBER_NOT_EXISTS);
    }

    @Test
    @Disabled  // TODO 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetPartyMemberPage() {
       // mock 数据
       PartyMemberDO dbPartyMember = randomPojo(PartyMemberDO.class, o -> { // 等会查询到
           o.setDebriefStudentId(null);
           o.setName(null);
           o.setStudentNumber(null);
           o.setBranchId(null);
           o.setCollegeId(null);
           o.setGradeId(null);
           o.setSatisfaction(null);
           o.setBaseSatisfaction(null);
           o.setDissatisfaction(null);
           o.setModifyTime(null);
           o.setCreateTime(null);
       });
       partyMemberMapper.insert(dbPartyMember);
       // 测试 debriefStudentId 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setDebriefStudentId(null)));
       // 测试 name 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setName(null)));
       // 测试 studentNumber 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setStudentNumber(null)));
       // 测试 branchId 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setBranchId(null)));
       // 测试 collegeId 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setCollegeId(null)));
       // 测试 gradeId 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setGradeId(null)));
       // 测试 satisfaction 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setSatisfaction(null)));
       // 测试 baseSatisfaction 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setBaseSatisfaction(null)));
       // 测试 dissatisfaction 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setDissatisfaction(null)));
       // 测试 modifyTime 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setModifyTime(null)));
       // 测试 createTime 不匹配
       partyMemberMapper.insert(cloneIgnoreId(dbPartyMember, o -> o.setCreateTime(null)));
       // 准备参数
       PartyMemberPageReqVO reqVO = new PartyMemberPageReqVO();
       reqVO.setDebriefStudentId(null);
       reqVO.setName(null);
       reqVO.setStudentNumber(null);
       reqVO.setBranchId(null);
       reqVO.setCollegeId(null);
       reqVO.setGradeId(null);
       reqVO.setSatisfaction(null);
       reqVO.setBaseSatisfaction(null);
       reqVO.setDissatisfaction(null);
       reqVO.setModifyTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<PartyMemberDO> pageResult = partyMemberService.getPartyMemberPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbPartyMember, pageResult.getList().get(0));
    }

}