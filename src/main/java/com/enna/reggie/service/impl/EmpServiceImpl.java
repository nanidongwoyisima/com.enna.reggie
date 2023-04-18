package com.enna.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enna.reggie.mapper.EmpMapper;
import com.enna.reggie.pojo.Employee;
import com.enna.reggie.service.EmpService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/17/22:16
 * @Description:
 */
@Service
public class EmpServiceImpl extends ServiceImpl<EmpMapper, Employee>implements EmpService {

    @Override
    public void add(HttpServletRequest request,Employee employee) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

    }
}
