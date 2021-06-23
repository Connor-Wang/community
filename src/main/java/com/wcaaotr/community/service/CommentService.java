package com.wcaaotr.community.service;

import com.wcaaotr.community.dao.CommentMapper;
import com.wcaaotr.community.entity.Comment;
import com.wcaaotr.community.util.CommunityConstant;
import com.wcaaotr.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author Connor
 * @create 2021-06-22-22:44
 */
@Service
public class CommentService {

    @Autowired(required = false)
    private CommentMapper commentMapper;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 根据实体查询评论
     * @param entityType 实体的类型
     * @param entityId 实体的 id
     * @return 评论列表
     */
    public List<Comment> findCommentsByEntity(Integer entityType, Integer entityId) {
        return commentMapper.selectCommentsByEntity(entityType, entityId);
    }

    public int findCommentCount(Integer entityType, Integer entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        if(comment == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        if(comment.getTargetId() == null){
            comment.setTargetId(0);
        }
        int rows = commentMapper.insertComment(comment);
        // 评论的是帖子的时候，更新帖子的评论数量
        if(comment.getEntityType() == CommunityConstant.ENTITY_TYPE_POST) {
            int commentCount = commentMapper.selectCountByEntity(CommunityConstant.ENTITY_TYPE_POST, comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), commentCount);
        }
        return rows;
    }
}
