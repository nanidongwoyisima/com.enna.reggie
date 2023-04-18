package com.enna.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enna.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/17/22:14
 * @Description:
 */
@Mapper
public interface EmpMapper extends BaseMapper<Employee> {
}
