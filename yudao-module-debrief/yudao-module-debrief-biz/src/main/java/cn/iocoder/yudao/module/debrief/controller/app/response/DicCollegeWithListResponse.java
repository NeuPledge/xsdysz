package cn.iocoder.yudao.module.debrief.controller.app.response;

import lombok.Data;

import java.util.List;

@Data
public class DicCollegeWithListResponse {
    private List<DicCollegeResponse> colleges;
    private List<String> collegeNames;
}
