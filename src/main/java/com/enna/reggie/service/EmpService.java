package com.enna.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.enna.reggie.pojo.Employee;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/17/22:16
 * @Description:
 */
public interface EmpService extends IService<Employee> {
    void add(HttpServletRequest request,Employee employee);


    void update(HttpServletRequest request, Employee employee);
}
