package com.spike.cloud.service;


import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.spike.cloud.config.RabbitConfig;
import com.spike.cloud.dao.SecKillDao;
import com.spike.cloud.entities.Item;
import com.spike.cloud.entities.SecKillOrder;
import com.spike.cloud.lock.SecKillLock;
import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author spike
 * @Date: 2020-05-12 23:45
 */
@Service
@DefaultProperties(defaultFallback = "secKill_TimeOutHandler")
@Slf4j
public class SecKillServiceImpl implements SecKillService {
    private static final String SECKILL_LOCK_PREFIX = "SecKill_item_lock::";
    private static final String SECKILL_QUANTITY_PREFIX = "secKill_item_quantity::";
    private static final String SECKILL_BOUGHT_STATUS = "secKill_bought_status::";
    private List<String> list = new ArrayList<>();
    private ConcurrentHashMap<String, Integer> soldOutMap = new ConcurrentHashMap<>();

    @Autowired
    private SecKillLock lock;

    @Autowired
    private SecKillDao secKillDao;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public int addItem(Item item) {
        return secKillDao.addItem(item);
    }

    @Override
    public boolean loadSecKillItems() {
        // load SecKill Items from Redis
        List<Item> list = secKillDao.getItemList();
        for (Item item : list) {
            String key = SECKILL_QUANTITY_PREFIX + item.getId();
            log.info("quantity: " + item.getQuantity());
            redisTemplate.opsForValue().set(key, item.getQuantity());
            log.info("" + redisTemplate.opsForValue().get(key));
        }
        log.info("Load SecKill Items form MySQL to Redis Successfully!");
        return true;
    }

    @Override
    public Item getItemById(String id) {
        return secKillDao.getItemById(id);
    }

    @Override
    public List<Item> getItemList() {
        return secKillDao.getItemList();
    }

    @Override
//    @HystrixCommand(fallbackMethod = "secKill_TimeOutHandler", commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
//    })
    @HystrixCommand
    @Transactional
    public String secKill(String user_id, String item_id) {
        if (soldOutMap.containsKey(item_id)) {
            log.info("sold out");
            return "sold out";
        }
        // check if user has bought this item once
        if (redisTemplate.opsForValue().get(SECKILL_BOUGHT_STATUS + user_id) != null) {
            return  "You have bought this item!";
        }
        String key = SECKILL_LOCK_PREFIX + item_id;
        String value = UUID.randomUUID() + user_id;
//        RLock lock = redissonClient.getLock(key);
        try {
            // try to lock this item
//            Boolean res = lock.tryLock(10, 10, TimeUnit.SECONDS);
            boolean res = lock.lock(key, value, "500");
            if (res) {
                // check if user has bought this item once
                if (redisTemplate.opsForValue().get(SECKILL_BOUGHT_STATUS + user_id) == null) {
                    // check stock
                    int quantity = (int)redisTemplate.opsForValue().get(SECKILL_QUANTITY_PREFIX + item_id);
                    if (quantity > 0) {
                        // reduce stock
                        redisTemplate.opsForValue().set(SECKILL_QUANTITY_PREFIX + item_id, quantity - 1);
                        log.info("quantity: " + (quantity - 1));
                        // set bought status
                        redisTemplate.opsForValue().set(SECKILL_BOUGHT_STATUS + user_id, 1);
                        // create order
//                        list.add(quantity + ": " + user_id + ": " + item_id);
                        sendOrder(new SecKillOrder(quantity,"order id", user_id, item_id, new Date()));
                    } else {
                        soldOutMap.put(item_id, 1);
                    }
                } else throw new Exception("user " + user_id + " has bought this secKill item!");
            }
        } catch(Exception e) {
            log.info(e.getMessage());
            return "You have bought this item!";
        } finally {
            lock.unlock(key, value);
//            lock.unlock();
        }
//        try {
//            // try to lock this item
//            Boolean res = lock.tryLock(10, 10, TimeUnit.SECONDS);
//            if (res) {
//                // check stock
//                int quantity = (int)redisTemplate.opsForValue().get(SECKILL_QUANTITY_PREFIX + item_id);
//                if (quantity > 0) {
//                    // reduce stock
//                    redisTemplate.opsForValue().set(SECKILL_QUANTITY_PREFIX + item_id, quantity - 1);
//                    log.info("quantity: " + (quantity - 1));
//                    // set bought status
//                    redisTemplate.opsForValue().set(SECKILL_BOUGHT_STATUS + user_id, 1);
//                    // create order
//                    list.add(user_id + " : " + item_id);
//                } else return "Fail!";
//            }
//        } catch(Exception e) {
//            log.info(e.getMessage());
//            return "Fail!";
//        } finally {
//            lock.unlock();
//        }
        return "Success!";
    }

    @Override
    public void sendOrder(SecKillOrder order) {
        log.info(order.toString());
        try {
            CorrelationData correlationData = new CorrelationData("secKill");
            rabbitTemplate.convertAndSend(RabbitConfig.ORDER_EXCHANGE_NAME, RabbitConfig.ORDER_ROUTING_KEY_NAME, order, correlationData);
        } catch (Exception e) {
            log.info("Order sent error!");
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getOrderList() {
        return list;
    }

    public String secKill_TimeOutHandler() {
        return "Busy...";
    }
}
