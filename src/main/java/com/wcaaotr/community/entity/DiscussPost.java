package com.wcaaotr.community.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

/**
 * @author Connor
 * @create 2021-06-16-9:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DiscussPost {

    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private Integer type;
    private Integer status;
    private Date createTime;
    private Integer commentCount;
    private Double score;

}
