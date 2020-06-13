package com.spike.cloud.controller;

import com.spike.cloud.entities.CommonResponse;
import com.spike.cloud.entities.Item;
import com.spike.cloud.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author spike
 * @Date: 2020-05-12 23:48
 */
@RestController
@RequestMapping(value = "/api/secKill")
@Slf4j
public class SecKillController {
    @Autowired
    private SecKillService secKillService;

    /**
     * add new secKill item into database
     * @param item: {String id, int quantity}
     * @return null
     */
    @PostMapping(value = "/item/add")
    public CommonResponse addItem(Item item) {
        int res = secKillService.addItem(item);
        log.info("insert result: " + res);
        if (res > 0) return new CommonResponse().Success(null);
        else return new CommonResponse().Fail("insert fail", null);
    }

    /**
     * get secKill item by id
     * @param id item id
     * @return item
     */
    @GetMapping(value = "/item/get/{id}")
    public CommonResponse getItemById(@PathVariable("id") String id) {
        Item item = secKillService.getItemById(id);
        log.info("get item: " + item);
        if (item != null) return new CommonResponse().Success(item);
        else return new CommonResponse().Fail("get item fail", null);
    }

    /**
     * get secKill item list
     * @return list of items
     */
    @GetMapping(value = "/item/get/list")
    public CommonResponse getItemList() {
        List<Item> list = secKillService.getItemList();
        return new CommonResponse().Success(list);
    }

    @GetMapping(value = "/item/secKill/{id}")
    public CommonResponse secKill(String session_id, @PathVariable("id") String item_id) {
//        HttpSession session = request.getSession();
//        if (session.getAttribute("isLogin") != null && (boolean)session.getAttribute("isLogin")) {
//            // use user_email as id
//            String user_id = (String)session.getAttribute("user_email");
//            String res = secKillService.secKill(user_id, item_id);
//            return new CommonResponse().Success(res);
//        } else return new CommonResponse().Fail("Please sign in!", null);
        // use sessionId as id
        String user_id = session_id;
        String res = secKillService.secKill(user_id, item_id);
        return new CommonResponse().Success(res);
    }

    @GetMapping(value = "/order/get/list")
    public CommonResponse getOrderList() {
        return new CommonResponse().Success(secKillService.getOrderList());
    }
}
