package com.enna.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enna.reggie.mapper.OrderDetailsMapper;
import com.enna.reggie.pojo.OrderDetail;
import com.enna.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/24/22:27
 * @Description:
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailsMapper, OrderDetail> implements OrderDetailService {
}
