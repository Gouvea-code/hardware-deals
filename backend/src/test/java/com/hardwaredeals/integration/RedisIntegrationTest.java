package com.hardwaredeals.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest @ActiveProfiles("test") @Testcontainers
class RedisIntegrationTest {
 @Container static GenericContainer<?> redis=new GenericContainer<>("redis:7-alpine").withExposedPorts(6379);
 @DynamicPropertySource static void redisProperties(DynamicPropertyRegistry registry){
  registry.add("spring.data.redis.host",redis::getHost);registry.add("spring.data.redis.port",()->redis.getMappedPort(6379));
 }
 @Autowired StringRedisTemplate values;
 @Test void writesReadsAndDeletesValue(){String key="hardware-deals:test:redis";values.opsForValue().set(key,"ready");
  assertThat(values.opsForValue().get(key)).isEqualTo("ready");values.delete(key);assertThat(values.hasKey(key)).isFalse();}
}
