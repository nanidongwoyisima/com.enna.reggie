package com.enna.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enna.reggie.common.R;
import com.enna.reggie.dto.DishDto;
import com.enna.reggie.pojo.Category;
import com.enna.reggie.pojo.Dish;
import com.enna.reggie.pojo.Employee;
import com.enna.reggie.service.CategoryService;
import com.enna.reggie.service.DishFlavorService;
import com.enna.reggie.service.DishService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    //菜品管理分页查询
    @GetMapping("/page")
    public R<Page> page(String name,Integer page, Integer pageSize){

        Page<Dish> pageInfo=new Page<>(page,pageSize);

        Page<DishDto> dishDtoPage=new Page<>();

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();

        dishLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName,name);

        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,dishLambdaQueryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list=records.stream().map((enna)->{

            DishDto dishDto=new DishDto();

            BeanUtils.copyProperties(enna,dishDto);

            Long categoryId = enna.getCategoryId();

            Category category = categoryService.getById(categoryId);
            //通过id获取到菜品分类名称
            if (category!=null) {
                String categoryName = category.getName();

                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());

            dishDtoPage.setRecords(list);

            return  R.success(dishDtoPage);
    }

    //
    @DeleteMapping
    public R<String> deleteId(@RequestBody List<Integer> ids){
        log.info("分类管理删除开始。。。");
        dishService.removeByIds(ids);
        return R.success("分类删除成功");
    }


    @PostMapping
    public R<String> sava(@RequestBody DishDto dishDto){
        log.info("新增信息:{}",dishDto);
        dishService.savaFlavor(dishDto);
        return  R.success("操作成功!");
    }

}