package com.spike.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


/**
 * @Author spike
 * @Date: 2020-05-23 22:02
 */
@SpringBootApplication
@EnableEurekaClient
public class secKillOrderMain8008 {
    public static void main(String[] args) {
        SpringApplication.run(secKillOrderMain8008.class, args);
    }
}
