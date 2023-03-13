package com.cmc.monggeul.global.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Component
@RequiredArgsConstructor
public class RedisDao {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setValues(String key, String data) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, data);
    }
    //        redisTemplate.opsForValue()
//                .set("RT:" + authentication.getName(),tokenDto.getRefreshToken(), Long.parseLong(String.valueOf(decodedJWT.getExpiresAt().getTime())), TimeUnit.MILLISECONDS);

    public void setValues(Authentication authentication, String refreshToken,Long time) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(authentication.getName(),refreshToken,time , TimeUnit.MILLISECONDS);
    }

    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        return (String) values.get(key);
    }

    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}