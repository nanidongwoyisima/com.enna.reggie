package com.enna.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enna.reggie.common.CustomException;
import com.enna.reggie.dto.DishDto;
import com.enna.reggie.dto.SetmealDto;
import com.enna.reggie.mapper.CategoryMapper;
import com.enna.reggie.mapper.SetmealMapper;
import com.enna.reggie.pojo.*;
import com.enna.reggie.service.CategoryService;
import com.enna.reggie.service.SetmealDishService;
import com.enna.reggie.service.SetmealService;
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
 * @Date: 2023/04/20/16:56
 * @Description:
 */
@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    public void updateByIdDish(SetmealDto setmealDto) {
        //修改第一张表
        this.updateById(setmealDto);
        Setmeal setmeal = this.getById(setmealDto.getId());
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
            setmealDishService.remove(queryWrapper);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
            setmealDishes.stream().map((enna)->{
                enna.setSetmealId(setmealDto.getId());
                return enna;
            }).collect(Collectors.toList());
            setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void remove(List<Long> ids) {

        //查询套餐状态是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count=this.count(queryWrapper);
        if (count>0){
            throw new CustomException("当前套餐正在售卖，无法删除！！！！");
        }

        //可以正常删除
        //先删Setmeal中的数据
        this.removeByIds(ids);

        //后删关联的数据
        //继续构造条件
        LambdaQueryWrapper<SetmealDish> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);

    }

    @Override
    // 新增套餐
    public void savaDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        Long setmealDtoId = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((enna)->{
            enna.setSetmealId(setmealDto.getId());
            return enna;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);

    }

    //修改套餐信息
    @Override
    public SetmealDto getByIdDish(Long id) {
//        //回显菜品信息
        Setmeal setmeal=this.getById(id);
        SetmealDto setmealDto=new SetmealDto();
        //进行对象拷贝，进行赋值
        BeanUtils.copyProperties(setmeal,setmealDto);
//        //构造条件查询表达式
        LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }
}
