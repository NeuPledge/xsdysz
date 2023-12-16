package cn.iocoder.yudao.module.debrief.controller.admin.app;

import cn.iocoder.yudao.module.debrief.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "数据下载")
@RestController
@RequestMapping("/file")
@Validated
public class DebriefFileController {

    @Autowired
    FileService fileService;


    /**
     * http://desktop-pt758cr:8011/file/immediate/1/college.xls
     */
    @Operation(summary = "实时下载指定学院的数据")
    @GetMapping("/immediate/{college_id}/college.xls")
    public void getCollegeData(@PathVariable("college_id") Long collegeId, HttpServletResponse response) {
        fileService.getCollegeFileImSharding(collegeId, response);
    }

/*    @ApiOperation("test")
    @GetMapping("/test")
    public ResponseEntity<FileSystemResource> test(@RequestParam("college_id") Long collegeId) {
        return fileService.getCollegeFileImSharding(collegeId);
    }*/


    /**
     * http://desktop-pt758cr:8011/file/immediate/comment/1/college_comment.xls
     */
    @Operation(summary = "实时下载指定学院的党员评价数据")
    @GetMapping("/immediate/comment/{college_id}/college_comment.xls")
    public void getCollegeCommentData(@PathVariable("college_id") Long collegeId, HttpServletResponse response) {
        fileService.getCollegeCommentSharding(collegeId, response);
    }


/*    @ApiOperation("实时下载全校党员党员评价数据")
    @GetMapping("/immediate/comment/all_colleges.xls")
    public ResponseEntity<FileSystemResource> getAllCollegesCommentData() {
        return fileService.getAllCollegesComment();
    }*/
}
