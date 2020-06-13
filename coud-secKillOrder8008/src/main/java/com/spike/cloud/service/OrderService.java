package com.spike.cloud.service;

import com.spike.cloud.entities.SecKillOrder;

/**
 * @Author spike
 * @Date: 2020-05-23 22:37
 */
public interface OrderService {
    int addOrder(String orderId, String customerId, String movieId);
}
