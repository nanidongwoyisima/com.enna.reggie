package com.enna.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enna.reggie.pojo.Orders;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/24/22:25
 * @Description:
 */
public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
