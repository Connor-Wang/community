package com.wcaaotr.community.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wcaaotr.community.dao.DiscussPostMapper;
import com.wcaaotr.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Connor
 * @create 2021-06-16-9:49
 */
@Service
public class DiscussPostService {

    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(Integer userId){
        return discussPostMapper.selectDiscussPosts(userId);
    }

    public int findDiscussPostRows(Integer userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
