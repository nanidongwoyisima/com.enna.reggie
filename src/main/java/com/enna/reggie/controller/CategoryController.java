package com.enna.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enna.reggie.common.R;
import com.enna.reggie.pojo.Category;
import com.enna.reggie.pojo.Employee;
import com.enna.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/20/10:30
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){

        categoryService.save(category);
        return R.success("新增成功...");

    }

    @GetMapping("/page")
    public R<Page> page( Integer page, Integer pageSize){

        Page<Category> pageInfo=new Page<>(page,pageSize);

        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();

        //根据最后排序处理
        queryWrapper.orderByDesc(Category::getSort);

        //调用MybatisPlus中的page()进行传参查找排序
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
            log.info("要修改的信息为:{}",category);
            categoryService.updateById(category);
            return  R.success("修改成功。。。");
    }
    @DeleteMapping
    public R<Category> delete(@PathVariable Long id){
        log.info("分类管理删除开始。。。");
        return R.error("删除失败...");
    }
}
