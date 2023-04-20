package com.enna.reggie.common;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/20/17:15
 * @Description:
 */

//自定义业务异常类
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
