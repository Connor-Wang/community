package com.wcaaotr.community.dao;

import com.wcaaotr.community.CommunityApplication;
import com.wcaaotr.community.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

/**
 * @author Connor
 * @create 2021-06-22-20:52
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class DiscussPostMapperTests {
    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;

    @Test
    void testSelectDiscussPost(){
        List<DiscussPost> discussPostList = discussPostMapper.selectDiscussPosts(0);
        for (DiscussPost discussPost : discussPostList) {
            System.out.println(discussPost);
        }
    }

    @Test
    void testAddDiscussPost(){
        DiscussPost post = new DiscussPost();
        post.setUserId(123);
        post.setTitle("123");
        post.setContent("456");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);
    }
}
