package com.jjynowcoder.community.controller;

import com.jjynowcoder.community.entity.Comment;
import com.jjynowcoder.community.entity.DiscussPost;
import com.jjynowcoder.community.entity.Page;
import com.jjynowcoder.community.entity.User;
import com.jjynowcoder.community.service.CommentService;
import com.jjynowcoder.community.service.DiscussPostService;
import com.jjynowcoder.community.service.UserService;
import com.jjynowcoder.community.util.CommunityConstant;
import com.jjynowcoder.community.util.CommunityUtil;
import com.jjynowcoder.community.util.HostHolder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path="/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user= hostHolder.getUser();
        if(user==null){
            return CommunityUtil.getJSONString(403,"请登录后再发表帖子哦！");
        }
        DiscussPost discussPost=new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        return CommunityUtil.getJSONString(0,"发布成功！");
    }

    @RequestMapping(path="/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId")int discussPostId, Model model, Page page){
        DiscussPost discussPost= discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",discussPost);
        User user=userService.findUserById(discussPost.getUserId());
        model.addAttribute("user",user);
        //评论分页信息
        page.setLimit(8);
        page.setPath("/discuss/detail/"+discussPostId);
        page.setRows(discussPost.getCommentCount());
        //帖子评论列表
        List<Comment> comments=commentService.findCommentsByEntity(ENTITY_TYPE_POST,
                discussPost.getId(), page.getOffset(),page.getLimit());
        List<Map<String,Object>> commentViews=new ArrayList<>();
        if(comments!=null){
            for(Comment c:comments){
                Map<String,Object> m=new HashMap<>();
                m.put("comment",c);
                m.put("user",userService.findUserById(c.getUserId()));
                //查询该评论的回复
                List<Comment> replies= commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT,
                        c.getId(),0,Integer.MAX_VALUE);
                List<Map<String,Object>> replyViews=new ArrayList<>();
                if(replies!=null){
                    for(Comment co:replies){
                        Map<String,Object> map=new HashMap<>();
                        map.put("comment",co);
                        map.put("user",userService.findUserById(co.getUserId()));
                        User target= co.getTargetId()==0?null:userService.findUserById(co.getTargetId());
                        map.put("target",target);
                        replyViews.add(map);
                    }
                }
                m.put("replys",replyViews);
                //回复数量
                int replyCnt= commentService.findCommentCount(ENTITY_TYPE_COMMENT,c.getId());
                m.put("replyCount",replyCnt);

                commentViews.add(m);
            }
        }
        model.addAttribute("comments",commentViews);
        return "/site/discuss-detail";
    }

}
