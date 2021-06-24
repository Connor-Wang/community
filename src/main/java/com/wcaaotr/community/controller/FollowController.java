package com.wcaaotr.community.controller;

import com.github.pagehelper.PageInfo;
import com.wcaaotr.community.entity.User;
import com.wcaaotr.community.service.FollowService;
import com.wcaaotr.community.service.UserService;
import com.wcaaotr.community.util.CommunityConstant;
import com.wcaaotr.community.util.CommunityUtil;
import com.wcaaotr.community.util.HostHolder;
import com.wcaaotr.community.util.RedisPageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author Connor
 * @create 2021-06-23-19:41
 */
@Controller
public class FollowController {
    @Autowired
    private FollowService followService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();

        followService.follow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0, "已关注！");
    }

    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType, int entityId) {
        User user = hostHolder.getUser();

        followService.unfollow(user.getId(), entityType, entityId);

        return CommunityUtil.getJSONString(0, "已取消关注！");
    }

    @RequestMapping(path = "/followees/{userId}", method = RequestMethod.GET)
    public String getFollowees(@PathVariable("userId")int userId, Model model,
                               @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                               @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize){
        User user = userService.findUserById(userId);
        if(user == null) {
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);

        int rows = (int) followService.findFolloweeCount(userId, CommunityConstant.ENTITY_TYPE_USER);
        PageInfo pageInfo = RedisPageUtil.getPageInfo(rows, pageSize, pageNum);
        model.addAttribute("pageInfo", pageInfo);

        List<Map<String, Object>> userList = followService.findFollowees(userId, (pageNum - 1) * pageSize, pageSize);
        if(userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);
        return "/site/followee";
    }

    @RequestMapping(path = "/followers/{userId}", method = RequestMethod.GET)
    public String getFollowers(@PathVariable("userId")int userId, Model model,
                               @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                               @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize){
        User user = userService.findUserById(userId);
        if(user == null) {
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);

        int rows = (int) followService.findFollowerCount(CommunityConstant.ENTITY_TYPE_USER, userId);
        PageInfo pageInfo = RedisPageUtil.getPageInfo(rows, pageSize, pageNum);
        model.addAttribute("pageInfo", pageInfo);

        List<Map<String, Object>> userList = followService.findFollowers(userId, (pageNum - 1) * pageSize, pageSize);
        if(userList != null) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);
        return "/site/follower";
    }

    public boolean hasFollowed(int userId){
        if(hostHolder.getUser() == null) {
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(), CommunityConstant.ENTITY_TYPE_USER, userId);
    }
}
