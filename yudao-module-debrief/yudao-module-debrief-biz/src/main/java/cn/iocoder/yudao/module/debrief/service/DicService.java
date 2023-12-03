package cn.iocoder.yudao.module.debrief.service;



import cn.iocoder.yudao.module.debrief.controller.app.response.DicCollegeResponse;
import cn.iocoder.yudao.module.debrief.controller.app.response.DicGradeResponse;

import java.util.List;

public interface DicService {

    String getBranchName(Long branchId);

    List<DicCollegeResponse> getColleges();

    List<DicGradeResponse> getGrades(Long collegeId);

    String getGrade(Long id);
}
