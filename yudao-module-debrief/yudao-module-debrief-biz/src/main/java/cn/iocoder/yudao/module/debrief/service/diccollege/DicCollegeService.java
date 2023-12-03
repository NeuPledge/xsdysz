package cn.iocoder.yudao.module.debrief.service.diccollege;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.debrief.controller.admin.diccollege.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 学院字典列 Service 接口
 *
 * @author 芋道源码
 */
public interface DicCollegeService {

    /**
     * 创建学院字典列
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDicCollege(@Valid DicCollegeSaveReqVO createReqVO);

    /**
     * 更新学院字典列
     *
     * @param updateReqVO 更新信息
     */
    void updateDicCollege(@Valid DicCollegeSaveReqVO updateReqVO);

    /**
     * 删除学院字典列
     *
     * @param id 编号
     */
    void deleteDicCollege(Long id);

    /**
     * 获得学院字典列
     *
     * @param id 编号
     * @return 学院字典列
     */
    DicCollegeDO getDicCollege(Long id);

    /**
     * 获得学院字典列分页
     *
     * @param pageReqVO 分页查询
     * @return 学院字典列分页
     */
    PageResult<DicCollegeDO> getDicCollegePage(DicCollegePageReqVO pageReqVO);

}