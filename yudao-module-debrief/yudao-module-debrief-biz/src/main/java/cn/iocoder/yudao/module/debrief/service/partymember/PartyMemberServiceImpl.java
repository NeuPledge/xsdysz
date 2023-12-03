package cn.iocoder.yudao.module.debrief.service.partymember;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.debrief.controller.admin.partymember.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.partymember.PartyMemberDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.debrief.dal.mysql.partymember.PartyMemberMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.debrief.enums.ErrorCodeConstants.*;

/**
 * 党员信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PartyMemberServiceImpl implements PartyMemberService {

    @Resource
    private PartyMemberMapper partyMemberMapper;

    @Override
    public Long createPartyMember(PartyMemberSaveReqVO createReqVO) {
        // 插入
        PartyMemberDO partyMember = BeanUtils.toBean(createReqVO, PartyMemberDO.class);
        partyMemberMapper.insert(partyMember);
        // 返回
        return partyMember.getId();
    }

    @Override
    public void updatePartyMember(PartyMemberSaveReqVO updateReqVO) {
        // 校验存在
        validatePartyMemberExists(updateReqVO.getId());
        // 更新
        PartyMemberDO updateObj = BeanUtils.toBean(updateReqVO, PartyMemberDO.class);
        partyMemberMapper.updateById(updateObj);
    }

    @Override
    public void deletePartyMember(Long id) {
        // 校验存在
        validatePartyMemberExists(id);
        // 删除
        partyMemberMapper.deleteById(id);
    }

    private void validatePartyMemberExists(Long id) {
        if (partyMemberMapper.selectById(id) == null) {
            throw exception(PARTY_MEMBER_NOT_EXISTS);
        }
    }

    @Override
    public PartyMemberDO getPartyMember(Long id) {
        return partyMemberMapper.selectById(id);
    }

    @Override
    public PageResult<PartyMemberDO> getPartyMemberPage(PartyMemberPageReqVO pageReqVO) {
        return partyMemberMapper.selectPage(pageReqVO);
    }

}