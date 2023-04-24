package com.enna.reggie.controller;

import com.enna.reggie.common.R;
import com.enna.reggie.pojo.Orders;
import com.enna.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 用户下单
 * @Date: 2023/04/24/22:22
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单信息:{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");

    }
}
