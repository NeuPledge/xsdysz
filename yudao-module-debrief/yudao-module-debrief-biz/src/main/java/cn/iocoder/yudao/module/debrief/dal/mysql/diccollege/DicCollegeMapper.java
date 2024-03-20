package cn.iocoder.yudao.module.debrief.dal.mysql.diccollege;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.debrief.controller.admin.diccollege.vo.*;
import org.apache.ibatis.annotations.Param;

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


    // 计算某个学院 本科生和研究生党员的总人数
    List<StypeCount> stypeCount(@Param("collegeId") Long collegeId);

    // 计算某个学院 本科生和研究生党员的总人数
    List<StypeCount> stypeCountAll(@Param("collegeIds") List<Long> collegeIds);

    // 参加测评学生人数
    Integer studentCount(@Param("collegeId") Long collegeId);

    // 多个学院参加测评学生人数
    Integer studentCountAll(@Param("collegeIds") List<Long> collegeIds);

}