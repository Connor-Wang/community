package com.wcaaotr.community.service;

import com.wcaaotr.community.dao.UserMapper;
import com.wcaaotr.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Connor
 * @create 2021-06-16-9:52
 */
@Service
public class UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    public User findUserById(Integer id){
        return userMapper.selectById(id);
    }
}
