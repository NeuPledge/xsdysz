package cn.iocoder.yudao.module.debrief.dal.mysql.diccollege;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.debrief.controller.admin.diccollege.vo.*;

/**
 * 学院字典列 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DicCollegeMapper extends BaseMapperX<DicCollegeDO> {

    default PageResult<DicCollegeDO> selectPage(DicCollegePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DicCollegeDO>()
                .likeIfPresent(DicCollegeDO::getCollegeName, reqVO.getCollegeName())
                .betweenIfPresent(DicCollegeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DicCollegeDO::getId));
    }

}