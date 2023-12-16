package cn.iocoder.yudao.module.debrief.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface FileService {
    // ResponseEntity<FileSystemResource> getCollegeFileIm(Long collegeId);

    void getCollegeFileImSharding(Long collegeId, HttpServletResponse response);

    // ResponseEntity<FileSystemResource> getCollegeComment(Long collegeId);

    void getCollegeCommentSharding(Long collegeId, HttpServletResponse response);

    // ResponseEntity<FileSystemResource> getAllCollegesFileIm();

    // ResponseEntity<FileSystemResource> getAllCollegesComment();
}
