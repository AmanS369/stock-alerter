package com.stockalert.alert_api.service;


import com.stockalert.entity.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;
    public void createRedisAlert(Alert savedAlert){
        String redisKey = String.format("alerts:%s:%s", savedAlert.getSymbol(), savedAlert.getCondition());
        String redisMember =  String.valueOf(savedAlert.getId());
        double priceScore = savedAlert.getTargetPrice();

        redisTemplate.opsForZSet().add(redisKey, redisMember, priceScore);
    }
}
