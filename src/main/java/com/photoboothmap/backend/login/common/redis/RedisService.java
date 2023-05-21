package com.photoboothmap.backend.login.common.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

// Redis 메서드화
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void setValues(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    // 만료시간 설정 -> 자동 삭제
    @Transactional
    public void setValuesWithTimeout(String key, String value, long timeout){
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    // 문제 발생.
    public String getValues(String key){
        log.info("redis getValues: 키 값 확인 = {}", key);
        // 키 값 정상이나 안찝힘
        return redisTemplate.opsForValue().get(key);
    }

    @Transactional
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}