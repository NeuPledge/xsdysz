package cn.iocoder.yudao.module.debrief.controller.app.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.debrief.controller.app.response.DicCollegeResponse;
import cn.iocoder.yudao.module.debrief.controller.app.response.DicCollegeWithListResponse;
import cn.iocoder.yudao.module.debrief.controller.app.response.DicGradeResponse;
import cn.iocoder.yudao.module.debrief.controller.app.response.DicGradeWithListResponse;
import cn.iocoder.yudao.module.debrief.service.DicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Tag(name = "Debrief-APP - 字典列")
@RestController
@RequestMapping(value = "/dic")
public class DicController {

    @Autowired
    private DicService dicService;

    @Operation(summary = "获取所有的学院列表")
    @GetMapping(value = "/college/all")
    public CommonResult getColleges() {
        List<DicCollegeResponse> colleges = dicService.getColleges();
        DicCollegeWithListResponse dicCollegeWithListResponse = new DicCollegeWithListResponse();
        dicCollegeWithListResponse.setColleges(colleges);
        List<String> collegeNames = new ArrayList<>();
        colleges.forEach(college -> collegeNames.add(college.getCollegeName()));
        dicCollegeWithListResponse.setCollegeNames(collegeNames);
        return success(dicCollegeWithListResponse);
    }

    @Operation(summary = "查询某学院下的班级")
    @GetMapping(value = "/college/grade/all")
    public CommonResult getGrades(
            @RequestParam(value = "college_id") Long collegeId) {
        List<DicGradeResponse> grades = dicService.getGrades(collegeId);
        DicGradeWithListResponse dicGradeWithListResponse = new DicGradeWithListResponse();
        dicGradeWithListResponse.setGrades(grades);
        List<String> gradeNames = new ArrayList<>();
        grades.forEach(grade -> gradeNames.add(grade.getGradeName()));
        dicGradeWithListResponse.setGradeNames(gradeNames);
        return success(dicGradeWithListResponse);
    }

    @GetMapping
    public CommonResult getGradeNameByGradeId(@RequestParam(value = "grade_id") Long gradeId) {
        return success(dicService.getGrade(gradeId));
    }
}
