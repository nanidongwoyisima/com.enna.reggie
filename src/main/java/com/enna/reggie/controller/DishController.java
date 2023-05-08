package com.enna.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enna.reggie.common.R;
import com.enna.reggie.dto.DishDto;
import com.enna.reggie.pojo.Category;
import com.enna.reggie.pojo.Dish;
import com.enna.reggie.pojo.DishFlavor;
import com.enna.reggie.pojo.Employee;
import com.enna.reggie.service.CategoryService;
import com.enna.reggie.service.DishFlavorService;
import com.enna.reggie.service.DishService;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
    @Autowired
    private RedisTemplate redisTemplate;

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

    // 批量删除操作
    @DeleteMapping
    public R<String> deleteId(@RequestParam List<Long> ids){
        log.info("分类管理删除开始。id为:{}",ids);
        dishService.delete(ids);
        return R.success("分类删除成功");
    }

    @PostMapping
    public R<String> sava(@RequestBody DishDto dishDto){
        log.info("新增信息:{}",dishDto);
        dishService.savaFlavor(dishDto);
        //清理相对应的缓存信息
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return  R.success("操作成功!");
    }


    //通过id回显数据
    @GetMapping("/{id}")
    public  R<DishDto> getId(@PathVariable  Long id){
        log.info("修改的菜品id为：{}",id);

        DishDto dishDto = dishService.getByIdFlavor(id);

        return R.success(dishDto);
    }

    //修改数据操作
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateByIdFlavor(dishDto);
        //清理相对应的缓存信息
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return  R.success("修改成功!");
    }

//    @GetMapping("/list")
//    public  R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        queryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }

    @GetMapping("/list")
    @Cacheable(value = "dishCache",key = "#dish.categoryId+'_'+#dish.status")
    public  R<List<DishDto>> list(Dish dish){
        //redis缓存优化
//        List<DishDto> dishDtoList;
//        //动态构造key
//        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
//        //从redis中获取缓存数据
//        dishDtoList= (List<DishDto>) redisTemplate.opsForValue().get(key);
//        if (dishDtoList!=null){
//            //如果存在，直接返回，无需查询数据库
//            return  R.success(dishDtoList);
//        }
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        //select * from Dish where dish_category_id=?
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        //select * from Dish where Status=1
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList=list.stream().map((enna)->{
            DishDto dishDto=new DishDto();

            BeanUtils.copyProperties(enna,dishDto);

            Long categoryId = enna.getCategoryId(); //分类id
            //通过id获取到菜品分类名称
            Category category = categoryService.getById(categoryId);
            //做条件判断
            if (category!=null) {
                String categoryName = category.getName();
                //对DishDto中CategoryName赋值
                dishDto.setCategoryName(categoryName);
            }
            Long ennaId = enna.getId();//从enna中活得id
            //构造条件查询
            LambdaQueryWrapper<DishFlavor> dtoLambdaQueryWrapper=new LambdaQueryWrapper<>();
            dtoLambdaQueryWrapper.eq(DishFlavor::getDishId,ennaId);
            // select * from dish_flavor where dish_id=?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dtoLambdaQueryWrapper);
            //赋值
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());
        //如果不存在，需要查询数据库。并缓存到redis中
//        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }

    // 批量起售和停售
    @PostMapping("/status/{status}")
    @CacheEvict(value = "dishCache",allEntries = true)
    public R<String> updateStatus(Long[] ids,@PathVariable int status){
        //将数组转为集合
        List<Long> idsList = Arrays.asList(ids);
        //创建更新的条件构造器
        LambdaUpdateWrapper<Dish> queryWrapper = new LambdaUpdateWrapper<>();
        //设置修改状态和条件
        queryWrapper.set(Dish::getStatus,status).in(Dish::getId,idsList);
        //执行更新操作
        dishService.update(queryWrapper);
        return R.success("操作成功！");
    }


}
