package com.enna.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enna.reggie.dto.SetmealDto;
import com.enna.reggie.pojo.Setmeal;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/20/16:55
 * @Description:
 */
public interface SetmealService extends IService<Setmeal> {


    void updateByIdDish(SetmealDto setmealDto);

    void remove(List<Long> ids);

    void savaDish(SetmealDto setmealDto);

    SetmealDto getByIdDish(Long id);
}
