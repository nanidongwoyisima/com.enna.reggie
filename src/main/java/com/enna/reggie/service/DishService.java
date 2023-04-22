package com.enna.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enna.reggie.dto.DishDto;
import com.enna.reggie.pojo.Dish;
import com.enna.reggie.pojo.Employee;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/20/16:55
 * @Description:
 */
public interface DishService extends IService<Dish> {

    public void savaFlavor(DishDto dishDto);

    public DishDto getByIdFlavor(Long id);

    void updateByIdFlavor(DishDto dishDto);
}
