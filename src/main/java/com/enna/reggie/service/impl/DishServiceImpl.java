package com.enna.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enna.reggie.common.CustomException;
import com.enna.reggie.dto.DishDto;
import com.enna.reggie.mapper.CategoryMapper;
import com.enna.reggie.mapper.DishMapper;
import com.enna.reggie.pojo.*;
import com.enna.reggie.service.CategoryService;
import com.enna.reggie.service.DishFlavorService;
import com.enna.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    @Override
    public DishDto getByIdFlavor(Long id) {
        //回显菜品信息
        Dish dish = this.getById(id);
        DishDto dishDto=new DishDto();
        //通过对象拷贝进行赋值
        BeanUtils.copyProperties(dish,dishDto);
        //回显口味做法
        //构造条件查询表达式
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>() ;

        queryWrapper.eq(DishFlavor::getDishId,dish.getId());

        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(dishFlavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateByIdFlavor(DishDto dishDto) {
        //修改第一张表
        this.updateById(dishDto);

        Dish dish = this.getById(dishDto.getId());

        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors=flavors.stream().map((enna) -> {

            enna.setDishId(dishDto.getId());

            return  enna;

        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void delete(List<Long> ids) {
        //查询套餐状态是否可以删除
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        queryWrapper.eq(Dish::getStatus,1);
        int count=this.count(queryWrapper);
        if (count>0){
            throw new CustomException("当前套餐正在售卖，无法删除！！！！");
        }

        //可以正常删除
        //先删Setmeal中的数据
        this.removeByIds(ids);

        //后删关联的数据
        //继续构造条件
        LambdaQueryWrapper<DishFlavor> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(queryWrapper1);
    }

}

