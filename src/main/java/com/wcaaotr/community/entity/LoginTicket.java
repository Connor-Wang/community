package com.wcaaotr.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * 登陆凭证
 * @author Connor
 * @create 2021-06-21-19:59
 */
@Data
public class LoginTicket {
    private Integer id;
    private Integer userId;
    private String ticket;
    private Integer status; // 0-有效，1-无效
    private Date expired; // 超时日期

}
