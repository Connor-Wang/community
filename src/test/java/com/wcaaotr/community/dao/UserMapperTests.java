package com.wcaaotr.community.dao;

import com.wcaaotr.community.CommunityApplication;
import com.wcaaotr.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

/**
 * @author Connor
 * @create 2021-06-22-20:52
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class UserMapperTests {
    @Autowired(required = false)
    private UserMapper userMapper;

    @Test
    void testSelectUser(){
        User user = userMapper.selectById(1);
        System.out.println(user);
        User aaa = userMapper.selectByName("aaa");
        System.out.println(aaa);
    }

    @Test
    void testInsertUser(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int i = userMapper.insertUser(user);
        System.out.println(i);
        System.out.println(user.getId());
    }

    @Test
    void testUpdateUser(){
        int i = userMapper.updateStatus(5, 1);
        System.out.println(i);

        int i1 = userMapper.updateHeader(5, "http://www.nowcoder.com/102.png");
        System.out.println(i1);

        int i2 = userMapper.updatePassword(5, "111111");
        System.out.println(i2);
    }
}
