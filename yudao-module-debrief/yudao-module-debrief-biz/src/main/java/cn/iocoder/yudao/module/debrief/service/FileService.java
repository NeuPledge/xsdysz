package cn.iocoder.yudao.module.debrief.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

public interface FileService {
    // ResponseEntity<FileSystemResource> getCollegeFileIm(Long collegeId);

    ResponseEntity<FileSystemResource> getCollegeFileImSharding(Long collegeId);

    // ResponseEntity<FileSystemResource> getCollegeComment(Long collegeId);

    ResponseEntity<FileSystemResource> getCollegeCommentSharding(Long collegeId);

    // ResponseEntity<FileSystemResource> getAllCollegesFileIm();

    // ResponseEntity<FileSystemResource> getAllCollegesComment();
}
