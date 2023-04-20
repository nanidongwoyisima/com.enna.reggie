package com.enna.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enna.reggie.common.R;
import com.enna.reggie.pojo.Dish;
import com.enna.reggie.pojo.Employee;
import com.enna.reggie.service.DishService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/20/19:43
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    //菜品管理分页查询
    @GetMapping("/page")
    public R<Page> page(String name,Integer page, Integer pageSize){

        Page<Dish> pageInfo=new Page<>(page,pageSize);

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName,name);

        dishLambdaQueryWrapper.orderByDesc(Dish::getSort);

        dishService.page(pageInfo,dishLambdaQueryWrapper);

        return  R.success(pageInfo);
    }

    //
    @DeleteMapping
    public R<String> deleteId(@RequestBody List<String> ids){
        log.info("分类管理删除开始。。。");
        dishService.removeByIds(ids);
        return R.success("分类删除成功");
    }

}
