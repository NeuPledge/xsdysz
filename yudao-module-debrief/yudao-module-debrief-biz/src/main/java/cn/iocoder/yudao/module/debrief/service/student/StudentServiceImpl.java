package cn.iocoder.yudao.module.debrief.service.student;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.debrief.controller.admin.student.vo.StudentImportRespVo;
import cn.iocoder.yudao.module.debrief.controller.admin.student.vo.StudentImportVo;
import cn.iocoder.yudao.module.debrief.controller.admin.student.vo.StudentPageReqVO;
import cn.iocoder.yudao.module.debrief.controller.admin.student.vo.StudentSaveReqVO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicbranch.DicBranchDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.diccollege.DicCollegeDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.dicgrade.DicGradeDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.partymember.PartyMemberDO;
import cn.iocoder.yudao.module.debrief.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicbranch.DicBranchMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.diccollege.DicCollegeMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.dicgrade.DicGradeMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.partymember.PartyMemberMapper;
import cn.iocoder.yudao.module.debrief.dal.mysql.student.StudentMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.debrief.enums.ErrorCodeConstants.IMPORT_DATA_ERROR;
import static cn.iocoder.yudao.module.debrief.enums.ErrorCodeConstants.STUDENT_NOT_EXISTS;

/**
 * 学生信息 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private PartyMemberMapper partyMemberMapper;

    @Resource
    private DicBranchMapper dicBranchMapper;

    @Resource
    private DicCollegeMapper dicCollegeMapper;

    @Resource
    private DicGradeMapper dicGradeMapper;

    @Override
    public Long createStudent(StudentSaveReqVO createReqVO) {
        // 插入
        StudentDO student = BeanUtils.toBean(createReqVO, StudentDO.class);
        studentMapper.insert(student);
        // 返回
        return student.getId();
    }

    @Override
    public void updateStudent(StudentSaveReqVO updateReqVO) {
        // 校验存在
        validateStudentExists(updateReqVO.getId());
        // 更新
        StudentDO updateObj = BeanUtils.toBean(updateReqVO, StudentDO.class);
        studentMapper.updateById(updateObj);
    }

    @Override
    public void deleteStudent(Long id) {
        // 校验存在
        validateStudentExists(id);
        // 删除
        studentMapper.deleteById(id);
    }

    private void validateStudentExists(Long id) {
        if (studentMapper.selectById(id) == null) {
            throw exception(STUDENT_NOT_EXISTS);
        }
    }

    @Override
    public StudentDO getStudent(Long id) {
        return studentMapper.selectById(id);
    }

    @Override
    public PageResult<StudentDO> getStudentPage(StudentPageReqVO pageReqVO) {
        return studentMapper.selectPage(pageReqVO);
    }

    @Override
    public StudentImportRespVo importIncreaseData(MultipartFile file) {
        List<StudentImportVo> studentImportVos = new ArrayList<>();
        try {
            studentImportVos = ExcelUtils.read(file, StudentImportVo.class);
        } catch (IOException e) {
            throw exception(IMPORT_DATA_ERROR);
        }
        checkEmpty(studentImportVos);
        checkStudentNumberRepeat(studentImportVos);

//            Long stdBefore = studentMapper.selectCount();
//            Long partyMemberBefore = partyMemberMapper.selectCount();


        List<DicBranchDO> dicBranchDOS = insertIncreaseBranchData(studentImportVos);
        Map<String, Long> branchMap = dicBranchDOS.stream().collect(Collectors.toMap(e -> e.getBranchName(), e -> e.getId()));
        List<DicCollegeDO> dicCollegeDOS = insertIncreaseCollegeData(studentImportVos);
        Map<String, Long> collegeMap = dicCollegeDOS.stream().collect(Collectors.toMap(e -> e.getCollegeName(), e -> e.getId()));
        List<DicGradeDO> dicGradeDOS = insertIncreaseGradeData(studentImportVos, collegeMap);
        Map<String, Long> gradeMap = dicGradeDOS.stream().collect(Collectors.toMap(e -> gradeKey(e.getGradeName(), e.getCollegeId()), e -> e.getId()));

        List<StudentDO> studentDOS = insertIncreaseStudentData(studentImportVos, collegeMap, gradeMap);
        Map<String, Long> studentMap = studentDOS.stream().collect(Collectors.toMap(e -> e.getStudentNumber(), e -> e.getId()));

        List<StudentImportVo> partyMembers = studentImportVos.stream().filter(e -> "是".equals(e.getIsPartyMember())).collect(Collectors.toList());
        insertIncreasePartyMemberData(partyMembers, branchMap, collegeMap, gradeMap, studentMap);


        StudentImportRespVo studentImportRespVo = new StudentImportRespVo();
//            studentImportRespVo.setStudentCountInFile(studentImportVos.size());
//            Long stdAfter = studentMapper.selectCount();
//            studentImportRespVo.setStudentCountInDb(stdAfter.intValue() - stdBefore.intValue());
//        if (aLong != studentImportVos.size()) {
//            studentImportRespVo.setStudentNotImport()
//        }
//            Long partyMemberAfter = partyMemberMapper.selectCount();
//            studentImportRespVo.setPartyMemberCountInFile(partyMembers.size());
//            studentImportRespVo.setPartyMemberCountInDb(partyMemberAfter.intValue() - partyMemberBefore.intValue());
        return studentImportRespVo;
    }

    @Override
    @Transactional
    public StudentImportRespVo importData(MultipartFile file) {
        List<StudentImportVo> studentImportVos = new ArrayList<>();
        try {
            studentImportVos = ExcelUtils.read(file, StudentImportVo.class);
        } catch (IOException e) {
            throw exception(IMPORT_DATA_ERROR);
        }
        checkEmpty(studentImportVos);
        checkStudentNumberRepeat(studentImportVos);

//        truncateData();
        try {
            Long stdBefore = studentMapper.selectCount();
            Long partyMemberBefore = partyMemberMapper.selectCount();


            List<DicBranchDO> dicBranchDOS = insertBranchData(studentImportVos);
            Map<String, Long> branchMap = dicBranchDOS.stream().collect(Collectors.toMap(e -> e.getBranchName(), e -> e.getId()));
            List<DicCollegeDO> dicCollegeDOS = insertCollegeData(studentImportVos);
            Map<String, Long> collegeMap = dicCollegeDOS.stream().collect(Collectors.toMap(e -> e.getCollegeName(), e -> e.getId()));
            List<DicGradeDO> dicGradeDOS = insertGradeData(studentImportVos, collegeMap);
            Map<String, Long> gradeMap = dicGradeDOS.stream().collect(Collectors.toMap(e -> e.getGradeName(), e -> e.getId()));

            List<StudentDO> studentDOS = insertStudentData(studentImportVos, collegeMap, gradeMap);
            Map<String, Long> studentMap = studentDOS.stream().collect(Collectors.toMap(e -> e.getStudentNumber(), e -> e.getId()));

            List<StudentImportVo> partyMembers = studentImportVos.stream().filter(e -> "是".equals(e.getIsPartyMember())).collect(Collectors.toList());
            insertPartyMemberData(partyMembers, branchMap, collegeMap, gradeMap, studentMap);


            StudentImportRespVo studentImportRespVo = new StudentImportRespVo();
            studentImportRespVo.setStudentCountInFile(studentImportVos.size());
            Long stdAfter = studentMapper.selectCount();
            studentImportRespVo.setStudentCountInDb(stdAfter.intValue() - stdBefore.intValue());
//        if (aLong != studentImportVos.size()) {
//            studentImportRespVo.setStudentNotImport()
//        }

            Long partyMemberAfter = partyMemberMapper.selectCount();
            studentImportRespVo.setPartyMemberCountInFile(partyMembers.size());
            studentImportRespVo.setPartyMemberCountInDb(partyMemberAfter.intValue() - partyMemberBefore.intValue());
            return studentImportRespVo;

        } catch (Exception exception) {

            throw new RuntimeException(exception.getMessage());

        }

    }

    private void truncateData() {
        dicBranchMapper.delete(new LambdaQueryWrapperX<>());
        dicCollegeMapper.delete(new LambdaQueryWrapperX<>());
        dicGradeMapper.delete(new LambdaQueryWrapperX<>());
        studentMapper.delete(new LambdaQueryWrapperX<>());
        partyMemberMapper.delete(new LambdaQueryWrapperX<>());
    }

    // 增量
    private List<DicBranchDO> insertIncreaseBranchData(List<StudentImportVo> studentImportVos) {
        List<DicBranchDO> dicBranchDOS = dicBranchMapper.selectList();
        List<String> existBranchNames = dicBranchDOS.stream().filter(e -> StringUtils.isNotEmpty(e.getBranchName())).map(e -> e.getBranchName()).distinct().collect(Collectors.toList());
        List<String> branchNames = studentImportVos.stream().filter(e -> StringUtils.isNotEmpty(e.getBranchName())).map(e -> e.getBranchName()).distinct().collect(Collectors.toList());

        // toBeInsert
        List<DicBranchDO> toBeInsert = new ArrayList<>();
        for (String branchName : branchNames) {
            if (existBranchNames.contains(branchName)) {
                continue;
            }
            DicBranchDO dicBranchDO = new DicBranchDO();
            dicBranchDO.setBranchName(branchName);
            toBeInsert.add(dicBranchDO);
        }
        if (CollectionUtil.isNotEmpty(toBeInsert)) {
            dicBranchMapper.insertBatch(toBeInsert);
        }

        // 已有的，加上新增的。合并返回。
        dicBranchDOS.addAll(toBeInsert);
        return dicBranchDOS;
    }

    private List<DicBranchDO> insertBranchData(List<StudentImportVo> studentImportVos) {
        List<String> branchNames = studentImportVos.stream().filter(e -> StringUtils.isNotEmpty(e.getBranchName())).map(e -> e.getBranchName()).distinct().collect(Collectors.toList());
        List<DicBranchDO> dicBranchDOS = new ArrayList<>();
        for (String branchName : branchNames) {
            DicBranchDO dicBranchDO = new DicBranchDO();
            dicBranchDO.setBranchName(branchName);
            dicBranchDOS.add(dicBranchDO);
        }
        dicBranchMapper.insertBatch(dicBranchDOS);

        return dicBranchDOS;
    }

    private List<DicCollegeDO> insertIncreaseCollegeData(List<StudentImportVo> studentImportVos) {
        List<DicCollegeDO> collegeDOS = dicCollegeMapper.selectList();
        List<String> existCollegeNames = collegeDOS.stream().filter(e -> StringUtils.isNotEmpty(e.getCollegeName())).map(e -> e.getCollegeName()).distinct().collect(Collectors.toList());
        List<String> collegeNames = studentImportVos.stream().filter(e -> StringUtils.isNotEmpty(e.getCollegeName())).map(e -> e.getCollegeName()).distinct().collect(Collectors.toList());

        List<DicCollegeDO> toBeInsert = new ArrayList<>();
        for (String collegeName : collegeNames) {
            if (existCollegeNames.contains(collegeName)) {
                continue;
            }
            DicCollegeDO dicCollegeDO = new DicCollegeDO();
            dicCollegeDO.setCollegeName(collegeName);
            toBeInsert.add(dicCollegeDO);
        }
        if (CollectionUtil.isNotEmpty(toBeInsert)) {
            dicCollegeMapper.insertBatch(toBeInsert);
        }
        // 已有的，加上新增的。合并返回。
        collegeDOS.addAll(toBeInsert);
        return collegeDOS;
    }

    private List<DicCollegeDO> insertCollegeData(List<StudentImportVo> studentImportVos) {
        List<String> collegeNames = studentImportVos.stream().filter(e -> StringUtils.isNotEmpty(e.getCollegeName())).map(e -> e.getCollegeName()).distinct().collect(Collectors.toList());
        List<DicCollegeDO> dicCollegeDOS = new ArrayList<>();
        for (String collegeName : collegeNames) {
            DicCollegeDO dicCollegeDO = new DicCollegeDO();
            dicCollegeDO.setCollegeName(collegeName);
            dicCollegeDOS.add(dicCollegeDO);
        }
        dicCollegeMapper.insertBatch(dicCollegeDOS);

        return dicCollegeDOS;
    }

    private String gradeKey(String gradeName, Long collegeId) {
        return gradeName + "_#_" + collegeId;
    }

    private List<DicGradeDO> insertIncreaseGradeData(List<StudentImportVo> studentImportVos, Map<String, Long> collegeMap) {
        List<DicGradeDO> dicGradeDOS = dicGradeMapper.selectList();
        // 班级名称_学院ID
        List<String> existGradeNames = dicGradeDOS.stream()
                .filter(e -> StringUtils.isNotEmpty(e.getGradeName()))
                .map(e -> gradeKey(e.getGradeName(), e.getCollegeId())).distinct().collect(Collectors.toList());

        List<String> importGradeNames = studentImportVos.stream().filter(e -> StringUtils.isNotEmpty(e.getGradeName()) && StringUtils.isNotEmpty(e.getCollegeName()))
                .map(e -> gradeKey(e.getGradeName(), collegeMap.get(e.getCollegeName()))).distinct().collect(Collectors.toList());

        List<DicGradeDO> toBeInsert = new ArrayList<>();

        for (String gradeNameId : importGradeNames) {
            if (existGradeNames.contains(gradeNameId)) {
                continue;
            }
            String[] split = gradeNameId.split("_#_");
            String gradeName = split[0];
            Long collegeId = Long.parseLong(split[1]);
            DicGradeDO dicCollegeDO = new DicGradeDO();
            dicCollegeDO.setCollegeId(collegeId);
            dicCollegeDO.setGradeName(gradeName);

            toBeInsert.add(dicCollegeDO);
        }

        if (CollectionUtil.isNotEmpty(toBeInsert)) {
            dicGradeMapper.insertBatch(toBeInsert);
        }

        // 已有的，加上新增的。合并返回。
        dicGradeDOS.addAll(toBeInsert);
        return dicGradeDOS;
    }

    private List<DicGradeDO> insertGradeData(List<StudentImportVo> studentImportVos, Map<String, Long> collegeMap) {
        // 班级名称_学院ID
        List<String> importGradeNames = studentImportVos.stream().filter(e -> StringUtils.isNotEmpty(e.getGradeName()) && StringUtils.isNotEmpty(e.getCollegeName()))
                .map(e -> e.getGradeName() + "_#_" + collegeMap.get(e.getCollegeName())).distinct().collect(Collectors.toList());

        List<DicGradeDO> dicGradeDOS = new ArrayList<>();
        for (String gradeNameId : importGradeNames) {

            String[] split = gradeNameId.split("_#_");
            String gradeName = split[0];
            Long collegeId = Long.parseLong(split[1]);
            DicGradeDO dicCollegeDO = new DicGradeDO();
            dicCollegeDO.setCollegeId(collegeId);
            dicCollegeDO.setGradeName(gradeName);

            dicGradeDOS.add(dicCollegeDO);
        }

        if (CollectionUtil.isNotEmpty(dicGradeDOS)) {
            dicGradeMapper.insertBatch(dicGradeDOS);
        }

        return dicGradeDOS;
    }

    private List<StudentDO> insertIncreaseStudentData(List<StudentImportVo> studentImportVos,
                                                      Map<String, Long> collegeMap,
                                                      Map<String, Long> gradeMap) {
        List<StudentDO> studentDOS = studentMapper.selectList();
        List<String> existStudentNumbers = studentDOS.stream().filter(e -> StringUtils.isNotEmpty(e.getStudentNumber())).map(e -> e.getStudentNumber()).distinct().collect(Collectors.toList());

        List<StudentDO> toBeInsert = new ArrayList<>();
        for (StudentImportVo studentImportVo : studentImportVos) {
            if (existStudentNumbers.contains(studentImportVo.getStudentNumber())) {
                continue;
            }
            StudentDO studentDO = new StudentDO();
            studentDO.setName(studentImportVo.getName());
            studentDO.setStudentNumber(studentImportVo.getStudentNumber());
            studentDO.setGradeName(studentImportVo.getGradeName());
            Long collegeId = collegeMap.get(studentImportVo.getCollegeName());
            String gradeKey = gradeKey(studentImportVo.getGradeName(), collegeId);
            Long gradeId = gradeMap.get(gradeKey);
            studentDO.setGradeId(gradeId);
            studentDO.setCollegeId(collegeId);
            toBeInsert.add(studentDO);
        }
        if (CollectionUtil.isNotEmpty(toBeInsert)) {
            studentMapper.insertBatch(toBeInsert);
        }

        studentDOS.addAll(toBeInsert);
        return studentDOS;
    }

    private List<StudentDO> insertStudentData(List<StudentImportVo> studentImportVos,
                                              Map<String, Long> collegeMap,
                                              Map<String, Long> gradeMap) {
        List<StudentDO> studentDOS = new ArrayList<>();
        for (StudentImportVo studentImportVo : studentImportVos) {
            StudentDO studentDO = new StudentDO();
            studentDO.setName(studentImportVo.getName());
            studentDO.setStudentNumber(studentImportVo.getStudentNumber());
            studentDO.setGradeName(studentImportVo.getGradeName());
            studentDO.setGradeId(gradeMap.get(studentImportVo.getGradeName()));
            studentDO.setCollegeId(collegeMap.get(studentImportVo.getCollegeName()));
            studentDOS.add(studentDO);
        }
        studentMapper.insertBatch(studentDOS);

        return studentDOS;
    }

    private void insertIncreasePartyMemberData(List<StudentImportVo> partyMembers,
                                               Map<String, Long> branchMap,
                                               Map<String, Long> collegeMap,
                                               Map<String, Long> gradeMap,
                                               Map<String, Long> studentMap
    ) {
        List<PartyMemberDO> partyMemberDOS = partyMemberMapper.selectList();
        List<String> existStudentNumbers = partyMemberDOS.stream().filter(e -> StringUtils.isNotEmpty(e.getStudentNumber())).map(e -> e.getStudentNumber()).distinct().collect(Collectors.toList());


        List<PartyMemberDO> toBeInsert = new ArrayList<>();
        for (StudentImportVo partyMember : partyMembers) {
            if (existStudentNumbers.contains(partyMember.getStudentNumber())) {
                continue;
            }
            PartyMemberDO partyMemberDO = new PartyMemberDO();
            partyMemberDO.setDebriefStudentId(studentMap.get(partyMember.getStudentNumber()));
            partyMemberDO.setName(partyMember.getName());
            partyMemberDO.setStudentNumber(partyMember.getStudentNumber());
            partyMemberDO.setBranchId(branchMap.get(partyMember.getBranchName()));
            Long collegeId = collegeMap.get(partyMember.getCollegeName());
            String gradeKey = gradeKey(partyMember.getGradeName(), collegeId);
            Long gradeId = gradeMap.get(gradeKey);
            partyMemberDO.setGradeId(gradeId);
            partyMemberDO.setCollegeId(collegeId);
            toBeInsert.add(partyMemberDO);
        }
        if (CollectionUtil.isNotEmpty(toBeInsert)) {
            partyMemberMapper.insertBatch(toBeInsert);
        }
    }

    private void insertPartyMemberData(List<StudentImportVo> partyMembers,
                                       Map<String, Long> branchMap,
                                       Map<String, Long> collegeMap,
                                       Map<String, Long> gradeMap,
                                       Map<String, Long> studentMap
    ) {
        List<PartyMemberDO> partyMemberDOS = new ArrayList<>();
        for (StudentImportVo partyMember : partyMembers) {
            PartyMemberDO partyMemberDO = new PartyMemberDO();
            partyMemberDO.setDebriefStudentId(studentMap.get(partyMember.getStudentNumber()));
            partyMemberDO.setName(partyMember.getName());
            partyMemberDO.setStudentNumber(partyMember.getStudentNumber());
            partyMemberDO.setBranchId(branchMap.get(partyMember.getBranchName()));
            partyMemberDO.setCollegeId(collegeMap.get(partyMember.getCollegeName()));
            partyMemberDO.setGradeId(gradeMap.get(partyMember.getGradeName()));

            partyMemberDOS.add(partyMemberDO);
        }

        partyMemberMapper.insertBatch(partyMemberDOS);

    }

    /**
     * 检测是否存在空值.
     * <p>
     * 姓名不能为空.
     * 学号不能为空.
     * 班级不能为空.
     * 学院不能为空.
     * 是否是党员不能为空。如果是党员，支部不能为空。
     */
    private void checkEmpty(List<StudentImportVo> studentImportVos) {
        List<String> nameEmpty = new ArrayList<>();
        List<String> studentNumberEmpty = new ArrayList<>();
        List<String> gradeEmpty = new ArrayList<>();
        List<String> collegeEmpty = new ArrayList<>();
        List<String> isPEmpty = new ArrayList<>();
        List<String> branchEmpty = new ArrayList<>();
        for (StudentImportVo studentImportVo : studentImportVos) {
            // 先做一遍trim.
            studentImportVo.setName(StringUtils.trim(studentImportVo.getName()));
            studentImportVo.setStudentNumber(StringUtils.trim(studentImportVo.getStudentNumber()));
            studentImportVo.setCollegeName(StringUtils.trim(studentImportVo.getCollegeName()));
            studentImportVo.setGradeName(StringUtils.trim(studentImportVo.getGradeName()));
            studentImportVo.setIsPartyMember(StringUtils.trim(studentImportVo.getIsPartyMember()));
            studentImportVo.setBranchName(StringUtils.trim(studentImportVo.getBranchName()));

            if (StringUtils.isEmpty(studentImportVo.getName())) {
                nameEmpty.add(studentImportVo.toString());
            } else if (StringUtils.isEmpty(studentImportVo.getStudentNumber())) {
                studentNumberEmpty.add(studentImportVo.toString());
            } else if (StringUtils.isEmpty(studentImportVo.getGradeName())) {
                gradeEmpty.add(studentImportVo.toString());
            } else if (StringUtils.isEmpty(studentImportVo.getCollegeName())) {
                collegeEmpty.add(studentImportVo.toString());
            } else if (StringUtils.isEmpty(studentImportVo.getIsPartyMember())) {
                isPEmpty.add(studentImportVo.toString());
            } else if ("是".equals(studentImportVo.getIsPartyMember()) && StringUtils.isEmpty(studentImportVo.getBranchName())) {
                branchEmpty.add(studentImportVo.toString());
            }
        }
        String format = "";
        if (CollectionUtil.isNotEmpty(nameEmpty)) {
            format += "姓名为空的数据行:" + CollectionUtil.join(nameEmpty, ", ");
        }
        if (CollectionUtil.isNotEmpty(studentNumberEmpty)) {
            format += "学号为空的数据行:" + CollectionUtil.join(studentNumberEmpty, ", ");
        }
        if (CollectionUtil.isNotEmpty(gradeEmpty)) {
            format += "班级为空的数据行:" + CollectionUtil.join(gradeEmpty, ", ");
        }
        if (CollectionUtil.isNotEmpty(collegeEmpty)) {
            format += "学院为空的数据行:" + CollectionUtil.join(collegeEmpty, ", ");
        }
        if (CollectionUtil.isNotEmpty(isPEmpty)) {
            format += "是否党员列为空的数据行:" + CollectionUtil.join(isPEmpty, ", ");
        }
        if (CollectionUtil.isNotEmpty(branchEmpty)) {
            format += "支部为空的数据行:" + CollectionUtil.join(branchEmpty, ", ");
        }
        if (StringUtils.isNotEmpty(format)) {
            throw new ServiceException(INTERNAL_SERVER_ERROR.getCode(), format);
        }
    }

    /**
     * 检测是否存在重复值.
     * 学号不能重复.
     */
    private void checkStudentNumberRepeat(List<StudentImportVo> studentImportVos) {
        List<String> invalidNumber = new ArrayList<>();
        for (StudentImportVo studentImportVo : studentImportVos) {
            if (!NumberUtil.isNumber(studentImportVo.getStudentNumber())) {
                invalidNumber.add(studentImportVo.getStudentNumber());
            }
        }
        if (invalidNumber.size() > 0) {
            throw new ServiceException(INTERNAL_SERVER_ERROR.getCode(), "以下学号检测到不是纯数字，请核查:" + CollectionUtil.join(invalidNumber, ", "));
        }
        long count = studentImportVos.stream().map(e -> e.getStudentNumber().trim()).distinct().count();
        if (studentImportVos.size() > count) {
            throw new ServiceException(INTERNAL_SERVER_ERROR.getCode(), "检查到" + (studentImportVos.size() - count) + "个重复的学号");
        }
    }


}