package com.enna.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enna.reggie.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/24/22:23
 * @Description:
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
