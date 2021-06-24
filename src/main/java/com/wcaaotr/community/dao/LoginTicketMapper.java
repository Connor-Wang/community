package com.wcaaotr.community.dao;

import com.wcaaotr.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Connor
 * @create 2021-06-21-20:05
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {

    int insertLoginTicket(LoginTicket loginTicket);

    LoginTicket selectByTicket(String ticket);

    int updateStatus(String ticket, Integer status);

}
