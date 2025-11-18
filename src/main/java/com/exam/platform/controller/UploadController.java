package com.exam.platform.controller;

import com.exam.platform.common.ApiResponse;
import com.exam.platform.service.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * File upload controller
 */
@RestController
@RequestMapping("/upload")
@Slf4j
@RequiredArgsConstructor
public class UploadController {
    
    private final FileStorageService fileStorageService;
    
    /**
     * Upload file
     * 
     * @param file File to upload
     * @param type File type (image, video, document)
     * @return File URL and metadata
     */
    @PostMapping
    public ApiResponse<UploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "type", defaultValue = "image") String type) {
        try {
            String url = fileStorageService.uploadFile(file, type);
            
            UploadResponse response = new UploadResponse(
                    url,
                    file.getSize(),
                    file.getOriginalFilename()
            );
            
            return ApiResponse.success("上传成功", response);
        } catch (Exception e) {
            log.error("File upload failed: {}", e.getMessage(), e);
            return ApiResponse.error("上传失败: " + e.getMessage());
        }
    }
    
    @Data
    @AllArgsConstructor
    public static class UploadResponse {
        private String url;
        private Long size;
        private String filename;
    }
}
