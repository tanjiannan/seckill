package com.spike.cloud;

import com.spike.cloud.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Author spike
 * @Date: 2020-05-19 01:51
 */
@Component
public class SecKillApplicationRunner implements ApplicationRunner {
    @Autowired
    private SecKillService secKillService;

    @Override
    public void run(ApplicationArguments args) {
        secKillService.loadSecKillItems();
    }
}
