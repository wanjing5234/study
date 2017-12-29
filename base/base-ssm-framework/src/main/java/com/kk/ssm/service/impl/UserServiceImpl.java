package com.kk.ssm.service.impl;

import com.kk.ssm.model.User;
import com.kk.ssm.service.IUserService;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements IUserService {

//    @Resource
//    private IUserDao userDao;

    @Override
    public User getUserById(int userId) {
        User user = new User();
        user.setUserId(1);
        user.setUsername("kk");
        user.setPassword("123456");
        return user;
    }

}