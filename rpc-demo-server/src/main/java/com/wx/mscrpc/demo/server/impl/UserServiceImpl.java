package com.wx.mscrpc.demo.server.impl;

import com.wx.mscrpc.annotation.RpcService;
import com.wx.mscrpc.api.User;
import com.wx.mscrpc.api.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/16 11:49
 * @Version 1.0
 */
@Slf4j
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUserByUserId(Integer id) {
        log.info("接收到用户id：{}", id);
        // 模拟从数据库中获取用户的行为
        Random random = new Random();
        User user = User.builder()
                .userName("张三")
                .id(id)
                .sex(random.nextBoolean())
                .build();
        // 返回查询对象
        return user;
    }
    @Override
    public Integer insertUserId(User user) {
        log.info("插入用户:{}", user);
        return 1;
    }

}
