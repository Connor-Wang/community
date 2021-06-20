package com.wcaaotr.community;

import com.wcaaotr.community.dao.DiscussPostMapper;
import com.wcaaotr.community.dao.UserMapper;
import com.wcaaotr.community.entity.DiscussPost;
import com.wcaaotr.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @author Connor
 * @create 2021-06-16-9:08
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

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

    @Test
    void testSelectDiscussPost(){
        List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(0);
        for (DiscussPost discussPost : discussPostList) {
            System.out.println(discussPost);
        }

    }
}
