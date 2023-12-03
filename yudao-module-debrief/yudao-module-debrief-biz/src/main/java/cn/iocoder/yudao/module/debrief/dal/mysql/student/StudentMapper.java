package cn.iocoder.yudao.module.debrief.dal.mysql.student;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.debrief.dal.dataobject.student.StudentDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.debrief.controller.admin.student.vo.*;

/**
 * 学生信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface StudentMapper extends BaseMapperX<StudentDO> {

    default PageResult<StudentDO> selectPage(StudentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StudentDO>()
                .likeIfPresent(StudentDO::getName, reqVO.getName())
                .eqIfPresent(StudentDO::getStudentNumber, reqVO.getStudentNumber())
                .eqIfPresent(StudentDO::getGradeId, reqVO.getGradeId())
                .likeIfPresent(StudentDO::getGradeName, reqVO.getGradeName())
                .eqIfPresent(StudentDO::getCollegeId, reqVO.getCollegeId())
                .betweenIfPresent(StudentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(StudentDO::getId));
    }

}