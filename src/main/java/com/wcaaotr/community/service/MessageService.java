package com.wcaaotr.community.service;

import com.wcaaotr.community.dao.MessageMapper;
import com.wcaaotr.community.entity.Message;
import com.wcaaotr.community.util.Page;
import com.wcaaotr.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.unbescape.html.HtmlEscape;

import java.util.List;

/**
 * @author Connor
 * @create 2021-06-23-10:57
 */
@Service
public class MessageService {
    @Autowired(required = false)
    private MessageMapper messageMapper;
    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> findConversations(Integer userId){
        return messageMapper.selectConversations(userId);
    }

    public int findConversationCount(Integer userId){
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId){
        return messageMapper.selectLetters(conversationId);
    }

    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnreadCount(Integer userId, String conversationId){
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids, 1);
    }

    public Message findLatestNotice(int userId, String topic){
        return messageMapper.selectLatestNotice(userId,topic);
    }

    public int findNoticeCount(int useId, String topic) {
        return messageMapper.selectNoticeCount(useId,topic);
    }

    public int findNoticeUnreadCount(int userId, String topic) {
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    public List<Message> findNotices(int userId, String topic){
        return messageMapper.selectNotices(userId, topic);
    }
}
