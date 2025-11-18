package com.exam.platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Learning Resource entity
 */
@Entity
@Table(name = "resources", indexes = {
    @Index(name = "idx_category", columnList = "category_id"),
    @Index(name = "idx_type", columnList = "type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Resource {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ResourceType type;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(length = 50)
    private String teacher;
    
    @Column(length = 500)
    private String cover;
    
    @Column(nullable = false, length = 500)
    private String url;
    
    @Column
    private Integer duration;
    
    @Column(name = "view_count")
    private Integer viewCount = 0;
    
    @Column
    private Integer status = 1;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public enum ResourceType {
        video, document
    }
}
