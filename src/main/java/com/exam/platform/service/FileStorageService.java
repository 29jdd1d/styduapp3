package com.exam.platform.service;

import com.exam.platform.config.TencentCosConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * File storage service using Tencent COS
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {
    
    private final COSClient cosClient;
    private final TencentCosConfig cosConfig;
    
    /**
     * Upload file to Tencent COS
     * 
     * @param file MultipartFile to upload
     * @param type File type (image, video, document)
     * @return URL of uploaded file
     */
    public String uploadFile(MultipartFile file, String type) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        
        // Generate unique filename
        String filename = type + "/" + UUID.randomUUID().toString() + extension;
        
        try (InputStream inputStream = file.getInputStream()) {
            // Set object metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            
            // Create upload request
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    cosConfig.getBucket(),
                    filename,
                    inputStream,
                    metadata
            );
            
            // Execute upload
            PutObjectResult result = cosClient.putObject(putObjectRequest);
            
            log.info("File uploaded successfully: {}", filename);
            
            // Return full URL
            return cosConfig.getBaseUrl() + "/" + filename;
        } catch (Exception e) {
            log.error("Failed to upload file to COS: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file", e);
        }
    }
    
    /**
     * Delete file from Tencent COS
     * 
     * @param fileUrl URL of the file to delete
     */
    public void deleteFile(String fileUrl) {
        try {
            String key = fileUrl.replace(cosConfig.getBaseUrl() + "/", "");
            cosClient.deleteObject(cosConfig.getBucket(), key);
            log.info("File deleted successfully: {}", key);
        } catch (Exception e) {
            log.error("Failed to delete file from COS: {}", e.getMessage(), e);
        }
    }
}
