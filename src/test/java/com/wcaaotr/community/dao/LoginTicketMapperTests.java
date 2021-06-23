package com.wcaaotr.community.dao;

import com.wcaaotr.community.CommunityApplication;
import com.wcaaotr.community.dao.DiscussPostMapper;
import com.wcaaotr.community.dao.LoginTicketMapper;
import com.wcaaotr.community.dao.UserMapper;
import com.wcaaotr.community.entity.DiscussPost;
import com.wcaaotr.community.entity.LoginTicket;
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
public class LoginTicketMapperTests {

    @Autowired(required = false)
    private LoginTicketMapper loginTicketMapper;

    @Test
    void testInsertLoginTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*60*10));
        int i = loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println(i);
    }

    @Test
    void testUpdateLoginTicket(){
        LoginTicket i = loginTicketMapper.selectByTicket("abc");
        loginTicketMapper.updateStatus("abc", 1);
        System.out.println(i);
    }

}
