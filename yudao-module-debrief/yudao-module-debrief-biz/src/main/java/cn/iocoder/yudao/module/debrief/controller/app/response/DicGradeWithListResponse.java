package cn.iocoder.yudao.module.debrief.controller.app.response;

import lombok.Data;

import java.util.List;

@Data
public class DicGradeWithListResponse {
    private List<DicGradeResponse> grades;
    private List<String> gradeNames;
}
