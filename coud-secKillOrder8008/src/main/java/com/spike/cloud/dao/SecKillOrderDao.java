package com.spike.cloud.dao;

import com.spike.cloud.entities.SecKillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author spike
 * @Date: 2020-05-23 22:16
 */
@Mapper
@Component
public interface SecKillOrderDao {
    int addOrder(SecKillOrder secKillOrder);
}
