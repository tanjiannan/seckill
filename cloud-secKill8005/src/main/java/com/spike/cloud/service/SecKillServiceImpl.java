package com.spike.cloud.service;

import com.netflix.discovery.converters.Auto;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.spike.cloud.dao.SecKillDao;
import com.spike.cloud.entities.Item;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    List<String> list = new ArrayList<>();


    @Autowired
    private SecKillDao secKillDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

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
        String key = SECKILL_LOCK_PREFIX + item_id;
        RLock lock = redissonClient.getLock(key);
//        try {
//            // try to lock this item
//            Boolean res = lock.tryLock(10, 10, TimeUnit.SECONDS);
//            if (res) {
//                // check if user has bought this item once
//                if (redisTemplate.opsForValue().get(SECKILL_BOUGHT_STATUS + user_id) != null) {
//                    // check stock
//                    int quantity = (int)redisTemplate.opsForValue().get(SECKILL_QUANTITY_PREFIX + item_id);
//                    if (quantity > 0) {
//                        // reduce stock
//                        redisTemplate.opsForValue().set(SECKILL_QUANTITY_PREFIX + item_id, quantity - 1);
//                        // set bought status
//                        redisTemplate.opsForValue().set(SECKILL_BOUGHT_STATUS + user_id, 1);
//                        // create order
//                        list.add(user_id + " : " + item_id);
//                    }
//                } else throw new Exception("user " + user_id + " has bought this secKill item!");
//            }
//        } catch(Exception e) {
//            log.info(e.getMessage());
//            return "You have bought this item!";
//        } finally {
//            lock.unlock();
//        }
        try {
            // try to lock this item
            Boolean res = lock.tryLock(10, 10, TimeUnit.SECONDS);
            if (res) {
                // check stock
                int quantity = (int)redisTemplate.opsForValue().get(SECKILL_QUANTITY_PREFIX + item_id);
                if (quantity > 0) {
                    // reduce stock
                    redisTemplate.opsForValue().set(SECKILL_QUANTITY_PREFIX + item_id, quantity - 1);
                    log.info("quantity: " + (quantity - 1));
                    // set bought status
                    redisTemplate.opsForValue().set(SECKILL_BOUGHT_STATUS + user_id, 1);
                    // create order
                    list.add(user_id + " : " + item_id);
                }
            }
        } catch(Exception e) {
            log.info(e.getMessage());
            return "Fail!";
        } finally {
            lock.unlock();
        }
        return "Success!";
    }

    @Override
    public List<String> getOrderList() {
        return list;
    }

    public String secKill_TimeOutHandler() {
        return "Busy...";
    }
}
