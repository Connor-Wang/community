package com.wcaaotr.community.dao;

import com.wcaaotr.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Connor
 * @create 2021-06-23-10:33
 */
@Mapper
public interface MessageMapper {

    // 查询当前用户的会话列表，针对每个会话返回一个最新的私信
    List<Message> selectConversations(Integer userId);

    // 查询当前用户的会话数量
    int selectConversationCount(Integer userId);

    // 查询某个会话的私信列表
    List<Message> selectLetters(String conversationId);

    // 查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);

    // 查询未读私信数量
    int selectLetterUnreadCount(Integer userId, String conversationId);

    // 新增消息
    int insertMessage(Message message);

    // 修改消息状态
    int updateStatus(List<Integer> ids, Integer status);

    // 查询某个主题下最新的通知
    Message selectLatestNotice(int userId, String topic);

    // 查询某个主题所包含通知的数量
    int selectNoticeCount(int userId, String topic);

    // 查询未读通知的数量
    int selectNoticeUnreadCount(int userId, String topic);

    // 通知某个主题所包含的通知列表
    List<Message> selectNotices(int userId, String topic);


}
