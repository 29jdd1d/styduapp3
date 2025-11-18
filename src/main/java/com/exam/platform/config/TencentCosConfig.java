package com.exam.platform.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tencent COS configuration
 */
@Configuration
@ConfigurationProperties(prefix = "tencent.cos")
@Data
public class TencentCosConfig {
    
    private String secretId;
    private String secretKey;
    private String region;
    private String bucket;
    private String baseUrl;
    
    @Bean
    public COSClient cosClient() {
        // Initialize Tencent COS credentials
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        
        // Initialize client configuration
        Region regionObj = new Region(region);
        ClientConfig clientConfig = new ClientConfig(regionObj);
        
        // Create COS client
        return new COSClient(cred, clientConfig);
    }
}
