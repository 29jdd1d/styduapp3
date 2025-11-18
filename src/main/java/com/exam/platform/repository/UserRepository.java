package com.exam.platform.repository;

import com.exam.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by openid
     */
    Optional<User> findByOpenid(String openid);
    
    /**
     * Check if openid exists
     */
    boolean existsByOpenid(String openid);
}
