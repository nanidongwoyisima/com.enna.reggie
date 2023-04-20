package com.enna.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enna.reggie.pojo.Dish;
import com.enna.reggie.pojo.DishFlavor;
import com.enna.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/20/16:54
 * @Description:
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
