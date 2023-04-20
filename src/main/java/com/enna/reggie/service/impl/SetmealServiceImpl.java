package com.enna.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enna.reggie.mapper.CategoryMapper;
import com.enna.reggie.mapper.SetmealMapper;
import com.enna.reggie.pojo.Category;
import com.enna.reggie.pojo.Setmeal;
import com.enna.reggie.service.CategoryService;
import com.enna.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
