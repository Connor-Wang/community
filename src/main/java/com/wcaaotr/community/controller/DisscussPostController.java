package com.wcaaotr.community.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wcaaotr.community.entity.Comment;
import com.wcaaotr.community.entity.DiscussPost;
import com.wcaaotr.community.entity.User;
import com.wcaaotr.community.service.CommentService;
import com.wcaaotr.community.service.DiscussPostService;
import com.wcaaotr.community.service.UserService;
import com.wcaaotr.community.util.CommunityConstant;
import com.wcaaotr.community.util.CommunityUtil;
import com.wcaaotr.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Connor
 * @create 2021-06-22-20:08
 */
@Controller
@RequestMapping("/discuss")
public class DisscussPostController {

    @Autowired
    private UserService userService;
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录!");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setType(0);
        post.setStatus(0);
        post.setCreateTime(new Date());
        post.setCommentCount(0);
        post.setScore(0.0);
        discussPostService.addDisscussPost(post);
        // 报错的情况，将来统一处理
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    @RequestMapping(path = "/detail/{id}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("id")int discussPostId, Model model,
                                 @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                                 @RequestParam(defaultValue = "5", value = "pageSize") Integer pageSize){
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);

        // 评论的分页信息
        PageHelper.startPage(pageNum, pageSize);

        // 评论：给帖子的评论
        // 回复：给评论的评论
        // 评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(CommunityConstant.ENTITY_TYPE_POST, post.getId());
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        // 评论 VO 列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if(commentList != null) {
            for (Comment comment : commentList) {
                // 评论 VO
                Map<String, Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment", comment);
                // 作者
                commentVo.put("user", userService.findUserById(comment.getUserId()));

                // 回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId());
                List<Map<String, Object>> replyVoList = new ArrayList<>();
                if(replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVo = new HashMap<>();
                        replyVo.put("reply", reply);
                        replyVo.put("user", userService.findUserById(reply.getUserId()));
                        // 回复的目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target", target);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys", replyVoList);
                commentVo.put("replyCount", commentService.findCommentCount(CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId()));
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments", commentVoList);
        model.addAttribute("pageInfo", pageInfo);
        int pageFrom = pageInfo.getPageNum() - 2 > 0 ? pageInfo.getPageNum() - 2 : 1;
        int pageTo = pageInfo.getPageNum() + 2 <= pageInfo.getPages() ? pageInfo.getPageNum() + 2 : pageInfo.getPages();
        String pagePath = "/discuss/detail/" + discussPostId;
        model.addAttribute("pageFrom", pageFrom);
        model.addAttribute("pageTo", pageTo);
        return "/site/discuss-detail";
    }
}
