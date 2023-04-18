package com.enna.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.enna.reggie.Utils.JwtUtils;
import com.enna.reggie.common.R;
import com.enna.reggie.pojo.Employee;
import com.enna.reggie.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/17/22:18
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmpController {
    @Autowired
    private EmpService empService;

    //登录功能实现
@PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        // 将页面提交的密码进行md5加密
        String password = employee.getPassword();
         password = DigestUtils.md5DigestAsHex(password.getBytes());
         //根据页面提交的用户名查询数据库  （重点）
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = empService.getOne(queryWrapper);
        //如果没有查询到则返回登录失败结果
        if (emp==null){
            return  R.error("登录失败");
        }
        //如果密码不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return  R.error("登录失败");
        }
        //如果员工状态为禁用,返回员工已禁用
        if (emp.getStatus()==0){
            return  R.error("该员工已经禁用");
        }
        //登录成功,将员工id存进session
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    //退出功能实现
    @PostMapping("/logout")
    public  R<String> logout(HttpServletRequest request){

    //清理Session中的用户id
        request.getSession().removeAttribute("employee");
    //返回退出成功结果
        return R.success("退出成功...");

    }

    //新增员工功能

    @PostMapping
    public  R<String> addEmployee(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工信息为: {}",employee.toString());
        empService.add(request,employee);
        empService.save(employee);
        return R.success("新增员工成功!");
    }
}
