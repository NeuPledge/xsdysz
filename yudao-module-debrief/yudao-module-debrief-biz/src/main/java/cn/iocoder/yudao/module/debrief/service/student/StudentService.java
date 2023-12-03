package cn.iocoder.yudao.module.debrief.service.student;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.debrief.controller.admin.student.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 学生信息 Service 接口
 *
 * @author 芋道源码
 */
public interface StudentService {

    /**
     * 创建学生信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStudent(@Valid StudentSaveReqVO createReqVO);

    /**
     * 更新学生信息
     *
     * @param updateReqVO 更新信息
     */
    void updateStudent(@Valid StudentSaveReqVO updateReqVO);

    /**
     * 删除学生信息
     *
     * @param id 编号
     */
    void deleteStudent(Long id);

    /**
     * 获得学生信息
     *
     * @param id 编号
     * @return 学生信息
     */
    StudentDO getStudent(Long id);

    /**
     * 获得学生信息分页
     *
     * @param pageReqVO 分页查询
     * @return 学生信息分页
     */
    PageResult<StudentDO> getStudentPage(StudentPageReqVO pageReqVO);

    StudentImportRespVo importData(MultipartFile file);
}