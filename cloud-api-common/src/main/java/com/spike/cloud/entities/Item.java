package com.spike.cloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author spike
 * @Date: 2020-05-12 22:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    String id;
    int quantity;
}
