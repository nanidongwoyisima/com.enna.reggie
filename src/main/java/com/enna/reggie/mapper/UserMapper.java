package com.enna.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.enna.reggie.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: 你的名字
 * @Date: 2023/04/23/15:41
 * @Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
