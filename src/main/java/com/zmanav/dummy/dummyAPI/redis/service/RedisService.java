package com.zmanav.dummy.dummyAPI.redis.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisService {

    private RedisTemplate<String, Map<String, Object>> redisTemplate;

    public RedisService(RedisTemplate<String, Map<String, Object>> redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
    }

    public void addValue(String key, String value){
        redisTemplate.opsForValue().append(key, value);
    }

    public Object getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }
}
