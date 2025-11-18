package com.exam.platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * User entity
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 64)
    private String openid;
    
    @Column(length = 100)
    private String nickname;
    
    @Column(length = 500)
    private String avatar;
    
    @Column(length = 20)
    private String phone;
    
    @Column(name = "target_school", length = 100)
    private String targetSchool;
    
    @Column(name = "target_major", length = 100)
    private String targetMajor;
    
    @Column(name = "exam_year")
    private Integer examYear;
    
    @Column(name = "study_days")
    private Integer studyDays = 0;
    
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
