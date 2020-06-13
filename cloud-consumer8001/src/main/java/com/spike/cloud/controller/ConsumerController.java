package com.spike.cloud.controller;

import com.spike.cloud.entities.CommonResponse;
import com.spike.cloud.service.SecKillFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author spike
 * @Date: 2020-06-08 15:50
 */
@RestController
@Slf4j
public class ConsumerController {
    @Autowired
    private SecKillFeignService secKillFeignService;

    @GetMapping(value = "secKill/{id}")
    public CommonResponse secKill(HttpServletRequest request, @PathVariable("id") String item_id) {
        String session_id = request.getSession().getId();
        return secKillFeignService.secKill(session_id, item_id);
    }
}
