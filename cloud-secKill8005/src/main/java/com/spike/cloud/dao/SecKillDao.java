package com.spike.cloud.dao;

import com.spike.cloud.entities.Item;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @Author spike
 * @Date: 2020-05-12 23:08
 */
@Mapper
@Component
public interface SecKillDao {
    int addItem(Item item);

    Item getItemById(@Param("id") String id);

    List<Item> getItemList();
}
