package com.cjs.example.lock.service.impl;

import com.cjs.example.lock.model.OrderModel;
import com.cjs.example.lock.repository.OrderRepository;
import com.cjs.example.lock.service.OrderService;
import com.cjs.example.lock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author ChengJianSheng
 * @date 2019-07-30
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StockService stockService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 乐观锁
     */
    @Override
    public String save(Integer userId, Integer productId) {
        int stock = stockService.getByProduct(productId);
        log.info("剩余库存：{}", stock);
        if (stock <= 0) {
            return null;
        }

        //  如果不加锁，必然超卖

        RLock lock = redissonClient.getLock("stock:" + productId);

        try {
            lock.lock(10, TimeUnit.SECONDS);

            String orderNo = UUID.randomUUID().toString().replace("-", "").toUpperCase();

            if (stockService.decrease(productId)) {

                OrderModel orderModel = new OrderModel();
                orderModel.setUserId(userId);
                orderModel.setProductId(productId);
                orderModel.setOrderNo(orderNo);
                Date now = new Date();
                orderModel.setCreateTime(now);
                orderModel.setUpdateTime(now);
                orderRepository.save(orderModel);

                return orderNo;
            }

        } catch (Exception ex) {
            log.error("下单失败", ex);
        } finally {
            lock.unlock();
        }

        return null;
    }

}
