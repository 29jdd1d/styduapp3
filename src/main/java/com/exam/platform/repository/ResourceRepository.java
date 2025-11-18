package com.exam.platform.repository;

import com.exam.platform.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Resource repository
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    
    /**
     * Find resources by category
     */
    Page<Resource> findByCategoryId(Integer categoryId, Pageable pageable);
    
    /**
     * Find resources by type
     */
    Page<Resource> findByType(Resource.ResourceType type, Pageable pageable);
    
    /**
     * Find resources by category and type
     */
    Page<Resource> findByCategoryIdAndType(Integer categoryId, Resource.ResourceType type, Pageable pageable);
    
    /**
     * Find resources by status
     */
    Page<Resource> findByStatus(Integer status, Pageable pageable);
}
