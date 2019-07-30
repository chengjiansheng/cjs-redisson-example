package com.cjs.example.lock.service;

/**
 * @author ChengJianSheng
 * @date 2019-07-26
 */
public interface StockService {

    int getByProduct(Integer productId);

    boolean decrease(Integer productId);

}
