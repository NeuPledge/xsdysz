package cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.vo;

import cn.iocoder.yudao.module.debrief.controller.admin.evaluateresult.dto.BranchProgressDto;
import lombok.Data;

import java.util.List;

@Data
public class BranchProgressRespVo {

    private List<BranchProgressDto> list;

    // 进度 (40/90)
    private String progress;


}
