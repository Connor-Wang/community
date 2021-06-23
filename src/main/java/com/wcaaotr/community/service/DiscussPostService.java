package com.wcaaotr.community.service;

import com.wcaaotr.community.dao.DiscussPostMapper;
import com.wcaaotr.community.entity.DiscussPost;
import com.wcaaotr.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Date;
import java.util.List;

/**
 * @author Connor
 * @create 2021-06-16-9:49
 */
@Service
public class DiscussPostService {

    @Autowired(required = false)
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 根据用户 id 查询该用户发的所有帖子
     * @param userId 用户 id
     * @return 帖子列表
     */
    public List<DiscussPost> findDiscussPosts(Integer userId){
        return discussPostMapper.selectDiscussPosts(userId);
    }

    public int findDiscussPostRows(Integer userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * 添加一个帖子
     * @param discussPost 帖子
     * @return 修改数据库行数
     */
    public int addDisscussPost(DiscussPost discussPost){
        if(discussPost == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        // 转义 html 标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        // 过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        return discussPostMapper.insertDiscussPost(discussPost);
    }

    /**
     * 根据帖子 id 查询指定的帖子
     * @param id 帖子 id
     * @return 查询到的帖子
     */
    public DiscussPost findDiscussPostById(Integer id){
        return discussPostMapper.selectDiscussPostById(id);
    }

    public int updateCommentCount(Integer id, Integer commentCount){
        return discussPostMapper.updateCommentCount(id, commentCount);
    }



}
