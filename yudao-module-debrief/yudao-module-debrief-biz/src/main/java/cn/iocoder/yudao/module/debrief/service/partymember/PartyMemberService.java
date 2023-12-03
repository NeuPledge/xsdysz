package cn.iocoder.yudao.module.debrief.service.partymember;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.debrief.controller.admin.partymember.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.partymember.PartyMemberDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 党员信息 Service 接口
 *
 * @author 芋道源码
 */
public interface PartyMemberService {

    /**
     * 创建党员信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPartyMember(@Valid PartyMemberSaveReqVO createReqVO);

    /**
     * 更新党员信息
     *
     * @param updateReqVO 更新信息
     */
    void updatePartyMember(@Valid PartyMemberSaveReqVO updateReqVO);

    /**
     * 删除党员信息
     *
     * @param id 编号
     */
    void deletePartyMember(Long id);

    /**
     * 获得党员信息
     *
     * @param id 编号
     * @return 党员信息
     */
    PartyMemberDO getPartyMember(Long id);

    /**
     * 获得党员信息分页
     *
     * @param pageReqVO 分页查询
     * @return 党员信息分页
     */
    PageResult<PartyMemberDO> getPartyMemberPage(PartyMemberPageReqVO pageReqVO);

}