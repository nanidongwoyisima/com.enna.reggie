package com.enna.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/18/17:46
 * @Description:
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
public class GlobalException {

    //异常处理方法
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public  R<String> exception(SQLIntegrityConstraintViolationException exception){
        log.error(exception.getMessage());

        if (exception.getMessage().contains("Duplicate entry")){
            String[] sp = exception.getMessage().split(" ");
            String msg=sp[2]+"已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }
}
