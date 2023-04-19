package com.enna.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/19/22:26
 * @Description:
 */
@Component
@Slf4j
public class MyMethodHander implements MetaObjectHandler {

    //插入操作自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
            log.info("公共字段自动填充【insert】");
            metaObject.setValue("createTime", LocalDateTime.now());
            metaObject.setValue("updateTime", LocalDateTime.now());
            metaObject.setValue("createUser", BaseContext.getCurrentId());
            metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }


    //更新操作自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());

    }
}
