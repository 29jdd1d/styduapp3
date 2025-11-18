package com.exam.platform.service;

import com.exam.platform.entity.Resource;
import com.exam.platform.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Resource service with Redis caching
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceService {
    
    private final ResourceRepository resourceRepository;
    private final RedisService redisService;
    
    private static final String RESOURCE_CACHE_PREFIX = "resource:";
    private static final String RESOURCE_VIEW_COUNT_PREFIX = "resource:view:";
    
    /**
     * Get resource by ID (with caching)
     */
    @Cacheable(value = "resources", key = "#id")
    public Optional<Resource> getResourceById(Long id) {
        log.info("Fetching resource from database: {}", id);
        return resourceRepository.findById(id);
    }
    
    /**
     * Get resources by category with pagination
     */
    public Page<Resource> getResourcesByCategory(Integer categoryId, Pageable pageable) {
        return resourceRepository.findByCategoryId(categoryId, pageable);
    }
    
    /**
     * Get resources by type with pagination
     */
    public Page<Resource> getResourcesByType(Resource.ResourceType type, Pageable pageable) {
        return resourceRepository.findByType(type, pageable);
    }
    
    /**
     * Save or update resource
     */
    @Transactional
    @CacheEvict(value = "resources", key = "#resource.id")
    public Resource saveResource(Resource resource) {
        return resourceRepository.save(resource);
    }
    
    /**
     * Delete resource
     */
    @Transactional
    @CacheEvict(value = "resources", key = "#id")
    public void deleteResource(Long id) {
        resourceRepository.deleteById(id);
    }
    
    /**
     * Increment view count using Redis
     */
    public void incrementViewCount(Long resourceId) {
        String key = RESOURCE_VIEW_COUNT_PREFIX + resourceId;
        redisService.increment(key);
        
        // Sync to database every 10 views
        Long viewCount = (Long) redisService.get(key);
        if (viewCount != null && viewCount % 10 == 0) {
            syncViewCountToDatabase(resourceId, viewCount);
        }
    }
    
    /**
     * Get view count from Redis
     */
    public Long getViewCount(Long resourceId) {
        String key = RESOURCE_VIEW_COUNT_PREFIX + resourceId;
        Object count = redisService.get(key);
        return count != null ? (Long) count : 0L;
    }
    
    /**
     * Sync view count to database
     */
    @Transactional
    public void syncViewCountToDatabase(Long resourceId, Long viewCount) {
        resourceRepository.findById(resourceId).ifPresent(resource -> {
            resource.setViewCount(viewCount.intValue());
            resourceRepository.save(resource);
            log.info("Synced view count to database for resource {}: {}", resourceId, viewCount);
        });
    }
    
    /**
     * Cache resource data in Redis
     */
    public void cacheResourceData(Long resourceId, Object data) {
        String key = RESOURCE_CACHE_PREFIX + resourceId;
        redisService.set(key, data, 1, TimeUnit.HOURS);
    }
    
    /**
     * Get cached resource data from Redis
     */
    public Object getCachedResourceData(Long resourceId) {
        String key = RESOURCE_CACHE_PREFIX + resourceId;
        return redisService.get(key);
    }
}
