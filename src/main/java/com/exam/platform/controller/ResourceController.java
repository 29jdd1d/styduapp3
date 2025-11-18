package com.exam.platform.controller;

import com.exam.platform.common.ApiResponse;
import com.exam.platform.entity.Resource;
import com.exam.platform.service.ResourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * Resource controller
 */
@RestController
@RequestMapping("/resources")
@Slf4j
@RequiredArgsConstructor
public class ResourceController {
    
    private final ResourceService resourceService;
    
    /**
     * Get resource by ID
     */
    @GetMapping("/{id}")
    public ApiResponse<Resource> getResource(@PathVariable Long id) {
        return resourceService.getResourceById(id)
                .map(resource -> {
                    // Increment view count
                    resourceService.incrementViewCount(id);
                    return ApiResponse.success(resource);
                })
                .orElse(ApiResponse.error(404, "资源不存在"));
    }
    
    /**
     * Get resources list
     */
    @GetMapping
    public ApiResponse<Page<Resource>> getResources(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<Resource> resources;
        
        if (categoryId != null && type != null) {
            resources = resourceService.getResourcesByCategory(categoryId, pageable);
        } else if (categoryId != null) {
            resources = resourceService.getResourcesByCategory(categoryId, pageable);
        } else if (type != null) {
            Resource.ResourceType resourceType = Resource.ResourceType.valueOf(type);
            resources = resourceService.getResourcesByType(resourceType, pageable);
        } else {
            resources = Page.empty();
        }
        
        return ApiResponse.success(resources);
    }
    
    /**
     * Get view count from Redis
     */
    @GetMapping("/{id}/view-count")
    public ApiResponse<Long> getViewCount(@PathVariable Long id) {
        Long viewCount = resourceService.getViewCount(id);
        return ApiResponse.success(viewCount);
    }
}
