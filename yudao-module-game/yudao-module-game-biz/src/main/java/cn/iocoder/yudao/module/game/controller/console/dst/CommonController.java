package cn.iocoder.yudao.module.game.controller.console.dst;


import cn.iocoder.yudao.module.game.common.ResultData;
import cn.iocoder.yudao.module.game.common.params.FeedbackParam;
import com.dooyo.service.ICommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "通用模块")
@RequestMapping(value = "/common")
public class CommonController {

    @Autowired
    ICommonService commonService;

    @Operation(summary = "意见反馈", description = "")
    @PostMapping("/feedback")
    public ResultData saveFeedback(@RequestBody FeedbackParam param) {
        commonService.saveFeedback(param);
        return ResultData.success();
    }
}
