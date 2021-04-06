package com.example.vhandler.service;

import com.example.vhandler.dto.VoucherHistoryDto;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SmsCacheServiceImpl implements CacheHandlerService<Long, VoucherHistoryDto>{

    private final RedisTemplate redisTemplate;

    @Override
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    @Override
    public String getCacheName() {
        return "sms-voucher-code";
    }
}
