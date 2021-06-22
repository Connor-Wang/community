package com.wcaaotr.community.util;

import com.wcaaotr.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有用户的信息，用于代替 session 对象
 * @author Connor
 * @create 2021-06-21-22:42
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
