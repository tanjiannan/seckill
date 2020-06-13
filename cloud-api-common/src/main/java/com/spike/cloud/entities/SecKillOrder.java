package com.spike.cloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author spike
 * @Date: 2020-05-23 22:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecKillOrder {
    int id;
    String orderId;
    String customerId;
    String movieId;
    Date saleDate;

    public SecKillOrder(String orderId, String customerId, String movieId, Date saleDate) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.movieId = movieId;
        this.saleDate = saleDate;
    }
}
