package com.wcaaotr.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wcaaotr.community.entity.DiscussPost;
import com.wcaaotr.community.entity.User;
import com.wcaaotr.community.service.DiscussPostService;
import com.wcaaotr.community.service.LikeService;
import com.wcaaotr.community.service.UserService;
import com.wcaaotr.community.util.CommunityConstant;
import com.wcaaotr.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Connor
 * @create 2021-06-16-13:15
 */
@Controller
public class HomeController {

    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model,
                               @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                               @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize){
        PageHelper.startPage(pageNum, pageSize);
        List<DiscussPost> allDiscussPost = discussPostService.findDiscussPosts(0);
        PageInfo<DiscussPost> pageInfo = new PageInfo<DiscussPost>(allDiscussPost);
        List<Map<String, Object>> list = new ArrayList<>();
        if(list != null){
            for (DiscussPost discussPost : allDiscussPost) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", discussPost);
                User user = userService.findUserById(discussPost.getUserId());
                map.put("user", user);

                long likeCount = likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_POST, discussPost.getId());
                map.put("likeCount",likeCount);

                list.add(map);
            }
        }
        model.addAttribute("discussPosts", list);
        model.addAttribute("pageInfo", pageInfo);
        String pagePath = "/index";
        model.addAttribute("pagePath", pagePath);
        return "index";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }
}
