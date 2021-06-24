package com.wcaaotr.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.wcaaotr.community.entity.Message;
import com.wcaaotr.community.entity.User;
import com.wcaaotr.community.service.MessageService;
import com.wcaaotr.community.service.UserService;
import com.wcaaotr.community.util.CommunityConstant;
import com.wcaaotr.community.util.CommunityUtil;
import com.wcaaotr.community.util.HostHolder;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @author Connor
 * @create 2021-06-23-11:01
 */
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/letter/list", method = RequestMethod.GET)
    public String getLetterList(Model model,
                                @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                                @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize){
        User user = hostHolder.getUser();
        // 会话列表
        PageHelper.startPage(pageNum, pageSize);
        List<Message> conversationList = messageService.findConversations(user.getId());
        PageInfo<Message> pageInfo = new PageInfo<Message>(conversationList);
        model.addAttribute("pageInfo", pageInfo);
        List<Map<String, Object>> conversations = new ArrayList<>();
        if(conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));
                int targetId = user.getId().equals(message.getFromId()) ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);

        // 查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/letter";
    }

    @RequestMapping(path = "/letter/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(Model model, @PathVariable("conversationId") String conversationId,
                                  @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                                  @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<Message> letterList = messageService.findLetters(conversationId);
        PageInfo<Message> pageInfo = new PageInfo<Message>(letterList);
        model.addAttribute("pageInfo", pageInfo);
        List<Map<String, Object>> letters = new ArrayList<>();
        if(letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);
        // 私信目标
        model.addAttribute("target", getLetterTarget(conversationId));
        // 设置已读
        List<Integer> ids = getLetterIds(letterList);
        System.out.println(ids.size());
        if(!ids.isEmpty()){
            messageService.readMessage(ids);
        }
        return"/site/letter-detail";
    }

    private User getLetterTarget(String conversationId){
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);
        if(hostHolder.getUser().getId() == id0) {
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }
    }

    private List<Integer> getLetterIds(List<Message> letterList){
        List<Integer> ids = new ArrayList<>();
        if(letterList != null) {
            for (Message message : letterList) {
                int userId = hostHolder.getUser().getId();
                int toId = message.getToId();
                if(userId == toId && message.getStatus() == 0){
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName, String content){
        User target = userService.findUserByName(toName);
        if(target == null) {
            return CommunityUtil.getJSONString(1, "目标用户不存在");
        }
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        message.setStatus(0);
        if(message.getFromId() < message.getToId()){
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);
    }

    @RequestMapping(path = "/notice/list", method = RequestMethod.GET)
    public String getNoticeList(Model model) {
        User user = hostHolder.getUser();
        // 查询评论类通知
        Message message = messageService.findLatestNotice(user.getId(), CommunityConstant.TOPIC_COMMENT);
        Map<String, Object> messageVO = new HashMap<>();
        if(message != null) {
            messageVO.put("message", message);
            String content = HtmlUtils.htmlUnescape(message.getContent());
            HashMap data = JSONObject.parseObject(content, HashMap.class);
            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));
            int count = messageService.findNoticeCount(user.getId(), CommunityConstant.TOPIC_COMMENT);
            int unread = messageService.findNoticeUnreadCount(user.getId(), CommunityConstant.TOPIC_COMMENT);
            messageVO.put("count", count);
            messageVO.put("unread", unread);
        }
        System.out.println("评论类通知 -> " + messageVO.size());
        model.addAttribute("commentNotice", messageVO);
        // 查询点赞类通知
        message = messageService.findLatestNotice(user.getId(), CommunityConstant.TOPIC_LIKE);
        messageVO = new HashMap<>();
        if(message != null) {
            messageVO.put("message", message);
            String content = HtmlUtils.htmlUnescape(message.getContent());
            HashMap data = JSONObject.parseObject(content, HashMap.class);
            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            messageVO.put("postId", data.get("postId"));
            int count = messageService.findNoticeCount(user.getId(), CommunityConstant.TOPIC_LIKE);
            int unread = messageService.findNoticeUnreadCount(user.getId(), CommunityConstant.TOPIC_LIKE);
            messageVO.put("count", count);
            messageVO.put("unread", unread);
        }
        model.addAttribute("likeNotice", messageVO);
        // 查询关注类通知
        message = messageService.findLatestNotice(user.getId(), CommunityConstant.TOPIC_FOLLOW);
        messageVO = new HashMap<>();
        if(message != null) {
            messageVO.put("message", message);
            String content = HtmlUtils.htmlUnescape(message.getContent());
            HashMap data = JSONObject.parseObject(content, HashMap.class);
            messageVO.put("user", userService.findUserById((Integer) data.get("userId")));
            messageVO.put("entityType", data.get("entityType"));
            messageVO.put("entityId", data.get("entityId"));
            int count = messageService.findNoticeCount(user.getId(), CommunityConstant.TOPIC_FOLLOW);
            int unread = messageService.findNoticeUnreadCount(user.getId(), CommunityConstant.TOPIC_FOLLOW);
            messageVO.put("count", count);
            messageVO.put("unread", unread);
        }
        model.addAttribute("followNotice", messageVO);

        //查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount", noticeUnreadCount);

        return "/site/notice";
    }

    @RequestMapping(path = "/notice/detail/{topic}", method = RequestMethod.GET)
    public String getNoticeDetail(@PathVariable("topic") String topic, Model model,
                                  @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                                  @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize) {
        User user = hostHolder.getUser();
        PageHelper.startPage(pageNum, pageSize);
        List<Message> noticeList = messageService.findNotices(user.getId(), topic);
        PageInfo<Message> pageInfo = new PageInfo<Message>(noticeList);
        model.addAttribute("pageInfo", pageInfo);

        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if(noticeList != null) {
            for (Message notice : noticeList) {
                Map<String, Object> map = new HashMap<>();
                map.put("notice", notice);
                String content = HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);
                map.put("user", userService.findUserById((Integer) data.get("userId")));
                map.put("entityType", data.get("entityType"));
                map.put("entityId", data.get("entityId"));
                map.put("postId", data.get("postId"));
                // 通知的作者
                map.put("fromUser", userService.findUserById(notice.getFromId()));

                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices", noticeVoList);

        // 设置已读
        List<Integer> ids = getLetterIds(noticeList);
        if(!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "/site/notice-detail";
    }

}
