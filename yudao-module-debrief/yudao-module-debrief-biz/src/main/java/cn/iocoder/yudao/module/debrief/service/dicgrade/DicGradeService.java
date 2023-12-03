package cn.iocoder.yudao.module.debrief.service.dicgrade;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.debrief.controller.admin.dicgrade.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade.DicGradeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 班级字典列 Service 接口
 *
 * @author 芋道源码
 */
public interface DicGradeService {

    /**
     * 创建班级字典列
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDicGrade(@Valid DicGradeSaveReqVO createReqVO);

    /**
     * 更新班级字典列
     *
     * @param updateReqVO 更新信息
     */
    void updateDicGrade(@Valid DicGradeSaveReqVO updateReqVO);

    /**
     * 删除班级字典列
     *
     * @param id 编号
     */
    void deleteDicGrade(Long id);

    /**
     * 获得班级字典列
     *
     * @param id 编号
     * @return 班级字典列
     */
    DicGradeDO getDicGrade(Long id);

    /**
     * 获得班级字典列分页
     *
     * @param pageReqVO 分页查询
     * @return 班级字典列分页
     */
    PageResult<DicGradeDO> getDicGradePage(DicGradePageReqVO pageReqVO);

}