package com.spike.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author spike
 * @Date: 2020-06-09 19:26
 */
@Configuration
@Slf4j
public class RabbitConfig {
    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        // confirm callback (check if msg sent to exchange
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
//                log.info("msg sent to Exchange!");
            } else {
                log.info("msg failed sending to Exchange, {}, cause: {}", correlationData, cause);
            }
        });

        // enable return callback
        rabbitTemplate.setMandatory(true);
        // return callback (check if msg sent from exchange to queue)
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("msg failed sending from exchange to queue: exchange: {}, route: {}, replyCode: {}, replyText: {}, message: {}",
                    exchange, routingKey, replyCode, replyText, message);
        });

        return rabbitTemplate;
    }

    public static final String ORDER_QUEUE_NAME = "secKill.order.queue";
    public static final String ORDER_EXCHANGE_NAME = "secKill.order.exchange";
    public static final String ORDER_ROUTING_KEY_NAME = "secKill.order.routing.key";

    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(ORDER_ROUTING_KEY_NAME);
    }

}
