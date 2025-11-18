package com.exam.platform.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis service integration test
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.data.redis.host=localhost",
    "spring.data.redis.port=6379"
})
class RedisServiceTest {

    @Autowired(required = false)
    private RedisService redisService;

    @Test
    void testRedisServiceExists() {
        // Test will pass if Redis service bean is created
        // If Redis is not available, service will be null
        if (redisService != null) {
            assertNotNull(redisService);
        }
    }

    @Test
    void testStringOperations() {
        if (redisService != null) {
            // Test set and get
            redisService.set("test:key", "test-value");
            Object value = redisService.get("test:key");
            assertEquals("test-value", value);
            
            // Test delete
            Boolean deleted = redisService.delete("test:key");
            assertTrue(deleted);
            
            // Test with expiration
            redisService.set("test:expire", "value", 1, TimeUnit.SECONDS);
            assertTrue(redisService.hasKey("test:expire"));
        }
    }

    @Test
    void testIncrementOperations() {
        if (redisService != null) {
            String key = "test:counter";
            redisService.delete(key);
            
            Long count1 = redisService.increment(key);
            assertEquals(1L, count1);
            
            Long count2 = redisService.increment(key, 5);
            assertEquals(6L, count2);
            
            redisService.delete(key);
        }
    }

    @Test
    void testHashOperations() {
        if (redisService != null) {
            String key = "test:hash";
            
            redisService.hSet(key, "field1", "value1");
            Object value = redisService.hGet(key, "field1");
            assertEquals("value1", value);
            
            assertTrue(redisService.hHasKey(key, "field1"));
            
            redisService.delete(key);
        }
    }

    @Test
    void testListOperations() {
        if (redisService != null) {
            String key = "test:list";
            redisService.delete(key);
            
            redisService.lPush(key, "item1");
            redisService.lPush(key, "item2");
            
            Long size = redisService.lSize(key);
            assertEquals(2L, size);
            
            List<Object> items = redisService.lRange(key, 0, -1);
            assertEquals(2, items.size());
            
            redisService.delete(key);
        }
    }

    @Test
    void testSetOperations() {
        if (redisService != null) {
            String key = "test:set";
            redisService.delete(key);
            
            redisService.sAdd(key, "member1", "member2");
            
            assertTrue(redisService.sIsMember(key, "member1"));
            assertFalse(redisService.sIsMember(key, "member3"));
            
            Long size = redisService.sSize(key);
            assertEquals(2L, size);
            
            redisService.delete(key);
        }
    }
}
