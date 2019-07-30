package com.cjs.example.lock.repository;

import com.cjs.example.lock.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author ChengJianSheng
 * @date 2019-07-30
 */
public interface OrderRepository extends JpaSpecificationExecutor<OrderModel>, JpaRepository<OrderModel, Integer> {
}
