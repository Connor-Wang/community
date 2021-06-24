package com.wcaaotr.community.dao;

import com.wcaaotr.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Connor
 * @create 2021-06-22-22:38
 */
@Mapper
public interface CommentMapper {

    List<Comment> selectCommentsByEntity(Integer entityType, Integer entityId);

    int selectCountByEntity(Integer entityType, Integer entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(Integer id);
}
