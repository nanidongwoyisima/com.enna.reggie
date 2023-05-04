package com.enna.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enna.reggie.common.R;
import com.enna.reggie.dto.OrdersDto;
import com.enna.reggie.pojo.AddressBook;
import com.enna.reggie.pojo.OrderDetail;
import com.enna.reggie.pojo.Orders;
import com.enna.reggie.pojo.User;
import com.enna.reggie.service.AddressBookService;
import com.enna.reggie.service.OrderDetailService;
import com.enna.reggie.service.OrderService;
import com.enna.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单信息:{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");

    }

    //分页查询用户订单信息
    @GetMapping("/page")
    public R<Page> pageOrder(Integer page,Integer pageSize,String number,String begin,String end){
        log.info("page,pageSize分别为：{},{}",page,pageSize);
        //构造分页
        Page<Orders> pageInfo=new Page<>(page,pageSize);

        //构造器
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.like(number != null,Orders::getId, number);

        queryWrapper.ge(begin != null,Orders::getOrderTime,begin);

        queryWrapper.le(end != null,Orders::getOrderTime,end);

        queryWrapper.orderByAsc(Orders::getOrderTime);

        orderService.page(pageInfo,queryWrapper);

        List<Orders> records = pageInfo.getRecords();

        List<Orders>list=records.stream().map((enna->{
            enna.setUserName("用户"+enna.getId());
            return enna;
        })).collect(Collectors.toList());

        pageInfo.setRecords(list);

        return R.success(pageInfo);
    }
    /**
     * 更改订单状态
     * @param orders
     * @return
     */
    @PutMapping
    public R<Orders> status(@RequestBody Orders orders){

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Orders::getId,orders.getId());

        orderService.updateById(orders);

        return R.success(orders);
    }

//    @GetMapping("/page")
//    public R<Page> page(int page, int pageSize, String number, String beginTime,String endTime){
//
//        log.info("开始时间和结束时间为：{}",beginTime+endTime);
//
//        Page<Orders> ordersPage = new Page<>(page,pageSize);
//        Page<OrdersDto> ordersDtoPage = new Page<>();
//
//        BeanUtils.copyProperties(ordersPage,ordersDtoPage,"records");
//
//        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//
//        queryWrapper.eq(number != null,Orders::getNumber,number);
//        queryWrapper.between(beginTime != null && endTime != null,Orders::getOrderTime,beginTime,endTime);
//
//        //订单列表
//        List<Orders> ordersList = orderService.list(queryWrapper);
//
//        List<OrdersDto> ordersDtoList = ordersList.stream().map(orders -> {
//
//            OrdersDto ordersDto = new OrdersDto();
//
//            BeanUtils.copyProperties(orders, ordersDto);
//
//            Long userId = orders.getUserId();
//
//            User user = userService.getById(userId);
//
//            ordersDto.setUserName(user.getName());
//
//            return ordersDto;
//        }).collect(Collectors.toList());
//
//        ordersDtoPage.setRecords(ordersDtoList);
//
//        return R.success(ordersDtoPage);
//    }

}
