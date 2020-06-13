package com.spike.cloud.service;

import com.spike.cloud.dao.SecKillOrderDao;
import com.spike.cloud.entities.SecKillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author spike
 * @Date: 2020-05-23 22:39
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private SecKillOrderDao orderDao;

    @Override
    public int addOrder(String orderId, String customerId, String movieId) {
        SecKillOrder order = new SecKillOrder("order id", customerId, movieId, new Date());
        return orderDao.addOrder(order);
    }
}
