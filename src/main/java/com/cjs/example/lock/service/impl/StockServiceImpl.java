package com.cjs.example.lock.service.impl;

import com.cjs.example.lock.constant.RedisKeyPrefixConstant;
import com.cjs.example.lock.service.StockService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author ChengJianSheng
 * @date 2019-07-26
 */
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public int getByProduct(Integer productId) {
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        String value = hashOperations.get(RedisKeyPrefixConstant.STOCK, String.valueOf(productId));
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Integer.valueOf(value);
    }

    @Override
    public boolean decrease(Integer productId) {
        int stock = getByProduct(productId);
        if (stock <= 0) {
            return false;
        }
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put(RedisKeyPrefixConstant.STOCK, String.valueOf(productId), String.valueOf(stock - 1));
        return true;
    }
}
