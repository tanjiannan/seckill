package com.spike.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author spike
 * @Date: 2020-06-08 15:37
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class ConsumerMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerMain8001.class, args);
    }
}
