package com.enna.reggie.common;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/19/23:14
 * @Description:
 */
//以线程为一个作用域
//基于ThreadLocal封装工具类 用于保存和获取当前登录用户的id
public class BaseContext {

    public static  ThreadLocal<Long>  threadLocal=new ThreadLocal<>();

    //设置值
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    //获取值
    public  static  Long getCurrentId(){
        return  threadLocal.get();
    }
}
