package com.wcaaotr.community.dao;

import com.wcaaotr.community.CommunityApplication;
import com.wcaaotr.community.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * @author Connor
 * @create 2021-06-23-10:50
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MessageTests {
    @Autowired(required = false)
    private MessageMapper messageMapper;

    @Test
    public void testSelectConversations(){
        List<Message> messages = messageMapper.selectConversations(111);
        for (Message message : messages) {
            System.out.println(message);
        }
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);
    }

    @Test
    public void testSelectLetters(){
        List<Message> messages = messageMapper.selectLetters("111_112");
        for (Message message : messages) {
            System.out.println(message);
        }
        int count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);
        int count1 = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count1);
    }
}
