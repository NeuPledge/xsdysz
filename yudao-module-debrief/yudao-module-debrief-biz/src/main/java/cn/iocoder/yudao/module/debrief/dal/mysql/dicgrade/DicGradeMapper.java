package cn.iocoder.yudao.module.debrief.dal.mysql.dicgrade;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade.DicGradeDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.debrief.controller.admin.dicgrade.vo.*;

/**
 * 班级字典列 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DicGradeMapper extends BaseMapperX<DicGradeDO> {

    default PageResult<DicGradeDO> selectPage(DicGradePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DicGradeDO>()
                .eqIfPresent(DicGradeDO::getCollegeId, reqVO.getCollegeId())
                .likeIfPresent(DicGradeDO::getGradeName, reqVO.getGradeName())
                .betweenIfPresent(DicGradeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DicGradeDO::getId));
    }

}