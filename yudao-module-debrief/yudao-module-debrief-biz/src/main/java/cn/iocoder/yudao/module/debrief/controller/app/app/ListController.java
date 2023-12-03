package cn.iocoder.yudao.module.debrief.controller.app.app;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.debrief.controller.app.request.SubmitCommentRequest;
import cn.iocoder.yudao.module.debrief.controller.app.response.ListResponse;
import cn.iocoder.yudao.module.debrief.service.ListService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "Debrief-APP - 列表")
@RestController
@RequestMapping(value = "/list")
public class ListController {

    @Autowired
    ListService listService;

    @GetMapping
    public CommonResult<ListResponse> getGradePartMembers(
            @RequestParam(value = "grade_id") Long gradeId,
            @RequestParam(value = "student_number") String studentNumber) {
        ListResponse response = listService.getPartyMemberByGradeIdFilter(gradeId, studentNumber);
        return success(response);
    }

    @PostMapping(value = "/submit")
    public CommonResult<Boolean> submitComment(
            @RequestBody SubmitCommentRequest submit
    ) {
        //todo 是否需要设置一个限制。就是每个人只能提交一次表单。
        return success(listService.submitComment(submit));
    }

}
