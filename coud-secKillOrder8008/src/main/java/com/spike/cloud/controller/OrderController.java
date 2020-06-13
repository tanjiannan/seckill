package com.spike.cloud.controller;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import com.spike.cloud.config.RabbitConfig;
import com.spike.cloud.entities.CommonResponse;
import com.spike.cloud.entities.SecKillOrder;
import com.spike.cloud.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


/**
 * @Author spike
 * @Date: 2020-05-23 23:18
 */
@RestController
@Slf4j
@RequestMapping(value = "/api/secKill/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping(value = "/add")
    public CommonResponse addOrder(@RequestParam("orderId") String orderId,
                                   @RequestParam("customerId") String customerId,
                                   @RequestParam("movieId") String movieId) {
        int res = orderService.addOrder(orderId, customerId, movieId);
        if (res == 0) return new CommonResponse(CommonResponse.FAIL,"create order error", null);
        else return new CommonResponse().Success(null);
    }

    @RabbitListener(queues = RabbitConfig.ORDER_QUEUE_NAME)
    public void createOrder(SecKillOrder order, Envelope envelope, Channel channel) throws IOException {
        addOrder(order.getOrderId(), order.getCustomerId(), order.getMovieId());
        channel.basicAck(envelope.getDeliveryTag(), false);
    }
}
