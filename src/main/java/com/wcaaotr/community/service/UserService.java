package com.wcaaotr.community.service;

import com.wcaaotr.community.dao.LoginTicketMapper;
import com.wcaaotr.community.dao.UserMapper;
import com.wcaaotr.community.entity.LoginTicket;
import com.wcaaotr.community.entity.User;
import com.wcaaotr.community.util.CommunityConstant;
import com.wcaaotr.community.util.CommunityUtil;
import com.wcaaotr.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Connor
 * @create 2021-06-16-9:52
 */
@Service
public class UserService {

    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired(required = false)
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 根据 ID 查询用户
     * @param id 查询用户的 id
     * @return User 查询到的用户
     */
    public User findUserById(Integer id){
        return userMapper.selectById(id);
    }

    /**
     * 注册用户
     * @param user 要注册的用户
     * @return 注册结果
     */
    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if(user == null){
             throw new IllegalArgumentException("参数不能为空");
        }
        if(StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        // 验证账号和邮箱
        User u = userMapper.selectByName(user.getUsername());
        if(u != null){
            map.put("usernameMsg", "该账号已存在");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if(u != null){
            map.put("emailMsg", "该邮箱已被注册");
        }

        //注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation-email", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }

    /**
     * 激活用户
     * @param userId 用户 id
     * @param code 激活码
     * @return 激活状态
     */
    public int activation(Integer userId, String code) {
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return CommunityConstant.ACTIVATION_REPEAT;
        } else if(user.getActivationCode().equals(code)) {
            userMapper.updateStatus(userId, 1);
            return CommunityConstant.ACTIVATION_SUCCESS;
        } else {
            return CommunityConstant.ACTIVATION_FAILURE;
        }
    }

    /**
     * 用户登陆
     * @param username 用户名
     * @param password 用户的明文密码
     * @return 登陆操作的状态
     */
    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if(StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }

        // 验证合法性
        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg", "该账号不存在");
            return map;
        }
        if(user.getStatus() == 0){
            map.put("usernameMsg", "该账号未激活");
            return map;
        }
        password = CommunityUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg", "密码不正确");
            return map;
        }

        // 生成登陆凭证
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(user.getId());
        ticket.setTicket(CommunityUtil.generateUUID());
        ticket.setStatus(0);
        ticket.setExpired(new Date(System.currentTimeMillis() + ((long)expiredSeconds) * 1000));
        loginTicketMapper.insertLoginTicket(ticket);

        map.put("ticket", ticket.getTicket());

        return map;
    }

    /**
     * 用户登出
     * 更改改凭证的状态
     * @param ticket 登陆凭证
     */
    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket, 1);
    }

    /**
     * 根据 ticket 查询 LoginTicket 对象
     * @param ticket 登陆凭证中的 ticket
     * @return 查询到的 LoginTicket 对象
     */
    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    /**
     * 根据 userId 更新用户的头像地址
     * @param userId 用户的 id
     * @param headerUrl 新的头像路径
     * @return 操作的数据条数
     */
    public int updateHeaderUrl(Integer userId, String headerUrl){
        return userMapper.updateHeader(userId, headerUrl);
    }

    /**
     * 根据 userId 更新用户的密码
     * @param userId 用户的 id
     * @param password 新的密码
     * @return 操作的数据条数
     */
    public int updatePassword(Integer userId, String password){
        return userMapper.updatePassword(userId, password);
    }
}
