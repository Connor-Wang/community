package com.wcaaotr.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author Connor
 * @create 2021-06-23-2:10
 */
@Data
public class Message {

    private Integer id;
    private Integer fromId;
    private Integer toId;
    private String conversationId;
    private String content;
    private Integer status;
    private Date createTime;
}
