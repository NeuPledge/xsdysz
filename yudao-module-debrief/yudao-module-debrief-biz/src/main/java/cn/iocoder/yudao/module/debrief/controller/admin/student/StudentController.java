package cn.iocoder.yudao.module.debrief.controller.admin.student;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import static cn.iocoder.yudao.framework.operatelog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.debrief.controller.admin.student.vo.*;
import cn.iocoder.yudao.module.debrief.dal.dataobject.student.StudentDO;
import cn.iocoder.yudao.module.debrief.service.student.StudentService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 学生信息")
@RestController
@RequestMapping("/debrief/student")
@Validated
public class StudentController {

    @Resource
    private StudentService studentService;

    @PostMapping("/create")
    @Operation(summary = "创建学生信息")
    @PreAuthorize("@ss.hasPermission('debrief:student:create')")
    public CommonResult<Long> createStudent(@Valid @RequestBody StudentSaveReqVO createReqVO) {
        return success(studentService.createStudent(createReqVO));
    }


    @PostMapping("/import")
    @Operation(summary = "导入初始数据")
    @PreAuthorize("@ss.hasPermission('debrief:student:create')")
    public CommonResult<StudentImportRespVo> importData(FileUploadReqVO uploadReqVO) {
        StudentImportRespVo studentImportRespVo = studentService.importData(uploadReqVO.getFile());
        return success(studentImportRespVo);
    }

    @PutMapping("/update")
    @Operation(summary = "更新学生信息")
    @PreAuthorize("@ss.hasPermission('debrief:student:update')")
    public CommonResult<Boolean> updateStudent(@Valid @RequestBody StudentSaveReqVO updateReqVO) {
        studentService.updateStudent(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除学生信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('debrief:student:delete')")
    public CommonResult<Boolean> deleteStudent(@RequestParam("id") Long id) {
        studentService.deleteStudent(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得学生信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('debrief:student:query')")
    public CommonResult<StudentRespVO> getStudent(@RequestParam("id") Long id) {
        StudentDO student = studentService.getStudent(id);
        return success(BeanUtils.toBean(student, StudentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得学生信息分页")
    @PreAuthorize("@ss.hasPermission('debrief:student:query')")
    public CommonResult<PageResult<StudentRespVO>> getStudentPage(@Valid StudentPageReqVO pageReqVO) {
        PageResult<StudentDO> pageResult = studentService.getStudentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, StudentRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出学生信息 Excel")
    @PreAuthorize("@ss.hasPermission('debrief:student:export')")
    @OperateLog(type = EXPORT)
    public void exportStudentExcel(@Valid StudentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<StudentDO> list = studentService.getStudentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "学生信息.xls", "数据", StudentRespVO.class,
                        BeanUtils.toBean(list, StudentRespVO.class));
    }

}