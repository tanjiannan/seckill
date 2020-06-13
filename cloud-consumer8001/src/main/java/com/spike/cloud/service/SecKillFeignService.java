package com.spike.cloud.service;

import com.spike.cloud.entities.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author spike
 * @Date: 2020-06-08 15:39
 */
@FeignClient(value = "CLOUD-SECKILL-SERVICE")
public interface SecKillFeignService {
    @GetMapping(value = "/api/secKill/item/secKill/{id}")
    CommonResponse secKill(@RequestParam("session_id") String session_id, @PathVariable("id") String item_id);
}
