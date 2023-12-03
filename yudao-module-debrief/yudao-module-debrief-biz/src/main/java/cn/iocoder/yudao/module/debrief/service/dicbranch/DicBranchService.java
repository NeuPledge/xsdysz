package cn.iocoder.yudao.module.debrief.service.dicbranch;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.debrief.controller.admin.dicbranch.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicbranch.DicBranchDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 支部字典列 Service 接口
 *
 * @author 芋道源码
 */
public interface DicBranchService {

    /**
     * 创建支部字典列
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDicBranch(@Valid DicBranchSaveReqVO createReqVO);

    /**
     * 更新支部字典列
     *
     * @param updateReqVO 更新信息
     */
    void updateDicBranch(@Valid DicBranchSaveReqVO updateReqVO);

    /**
     * 删除支部字典列
     *
     * @param id 编号
     */
    void deleteDicBranch(Long id);

    /**
     * 获得支部字典列
     *
     * @param id 编号
     * @return 支部字典列
     */
    DicBranchDO getDicBranch(Long id);

    /**
     * 获得支部字典列分页
     *
     * @param pageReqVO 分页查询
     * @return 支部字典列分页
     */
    PageResult<DicBranchDO> getDicBranchPage(DicBranchPageReqVO pageReqVO);

}