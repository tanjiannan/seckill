package com.spike.cloud;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author spike
 * @Date: 2020-05-12 21:01
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class secKillMain8005 {
    public static void main(String[] args) {
        SpringApplication.run(secKillMain8005.class, args);

    }
}
