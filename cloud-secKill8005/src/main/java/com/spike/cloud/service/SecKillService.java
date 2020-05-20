package com.spike.cloud.service;

import com.spike.cloud.entities.Item;

import java.util.List;

/**
 * @Author spike
 * @Date: 2020-05-12 23:44
 */
public interface SecKillService {
    boolean loadSecKillItems();

    int addItem(Item item);

    Item getItemById(String id);

    List<Item>  getItemList();

    String secKill(String user_id, String item_id);

    List<String> getOrderList();
}
