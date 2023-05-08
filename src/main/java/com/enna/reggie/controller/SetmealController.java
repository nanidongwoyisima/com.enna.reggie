package com.enna.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enna.reggie.common.R;
import com.enna.reggie.dto.DishDto;
import com.enna.reggie.dto.SetmealDto;
import com.enna.reggie.pojo.Category;
import com.enna.reggie.pojo.Dish;
import com.enna.reggie.pojo.Setmeal;
import com.enna.reggie.service.CategoryService;
import com.enna.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/22/21:18
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> page(String name, Integer page, Integer pageSize){

        Page<Setmeal> pageInfo=new Page<>(page,pageSize);

        Page<SetmealDto> SetmealDtoPage=new Page<>();

        LambdaQueryWrapper<Setmeal> SetLambdaQueryWrapper=new LambdaQueryWrapper<>();

        SetLambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName,name);

        SetLambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,SetLambdaQueryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,SetmealDtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list=records.stream().map((enna)->{

            SetmealDto setmealDto=new SetmealDto();

            BeanUtils.copyProperties(enna,setmealDto);
            //分类id
            Long categoryId = enna.getCategoryId();
            //根据分类id查分类名称
            Category category = categoryService.getById(categoryId);
            //通过id获取到菜品分类名称
            if (category!=null) {
                //分类名称
                String categoryName = category.getName();

                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;
        }).collect(Collectors.toList());

        SetmealDtoPage.setRecords(list);

        return  R.success(SetmealDtoPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.remove(ids);
        return R.success("套餐数据删除成功");
    }


    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> sava(@RequestBody SetmealDto setmealDto){
        log.info("新增信息:{}",setmealDto);
        setmealService.savaDish(setmealDto);
        return  R.success("操作成功!");
    }

    @GetMapping("/{id}")
    public  R<SetmealDto> getId(@PathVariable  Long id){
        log.info("修改的菜品id为：{}",id);
        SetmealDto setmealDto = setmealService.getByIdDish(id);
        return R.success(setmealDto);
    }

    @PutMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateByIdDish(setmealDto);
        return  R.success("修改成功!");
    }

    // 批量起售和停售
    @PostMapping("/status/{status}")
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> updateStatus(Long[] ids,@PathVariable int status){
        //将数组转为集合
        List<Long> idsList = Arrays.asList(ids);
        //创建更新的条件构造器
        LambdaUpdateWrapper<Setmeal> queryWrapper = new LambdaUpdateWrapper<>();
        //设置修改状态和条件
        queryWrapper.set(Setmeal::getStatus,status).in(Setmeal::getId,idsList);
        //执行更新操作
        setmealService.update(queryWrapper);
        return R.success("操作成功！");
    }

    @GetMapping("/list")
    @Cacheable(value="setmealCache",key="#setmeal.categoryId+'_'+#setmeal.status")
    public  R<List<Setmeal>> list( Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,1);
        queryWrapper.orderByAsc(Setmeal::getId).orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }





}
