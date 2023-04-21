package com.enna.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enna.reggie.dto.DishDto;
import com.enna.reggie.mapper.CategoryMapper;
import com.enna.reggie.mapper.DishMapper;
import com.enna.reggie.pojo.Category;
import com.enna.reggie.pojo.Dish;
import com.enna.reggie.pojo.DishFlavor;
import com.enna.reggie.service.CategoryService;
import com.enna.reggie.service.DishFlavorService;
import com.enna.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/20/16:55
 * @Description:
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void savaFlavor(DishDto dishDto) {
        this.save(dishDto);
        //获取dishflavor中的id
        Long dishId = dishDto.getId();
        //获取Flavor值
        List<DishFlavor> dishDtoFlavors = dishDto.getFlavors();
        //通过stream流对id赋值
        dishDtoFlavors=dishDtoFlavors.stream().map((enna) -> {
            enna.setDishId(dishId);
            return  enna;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishDtoFlavors);
        }

    }

