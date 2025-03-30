package com.jjynowcoder.community.controller;

import com.jjynowcoder.community.entity.Event;
import com.jjynowcoder.community.entity.Page;
import com.jjynowcoder.community.entity.User;
import com.jjynowcoder.community.event.EventProducer;
import com.jjynowcoder.community.service.FollowService;
import com.jjynowcoder.community.service.UserService;
import com.jjynowcoder.community.util.CommunityConstant;
import com.jjynowcoder.community.util.CommunityUtil;
import com.jjynowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController implements CommunityConstant {
    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path="/follow",method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType,int entityId){
         User user=hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "登录后才能进行关注哦！");
        }
         followService.follow(user.getId(),entityType,entityId);

        Event event=new Event().setTopic(TOPIC_FOLLOW).setUserId(hostHolder.getUser().getId()).setEntityType(entityType)
                .setEntityId(entityId).setEntityUserId(entityId);
        eventProducer.fireEvent(event);

        return CommunityUtil.getJSONString(0, "关注成功!");
    }

    @RequestMapping(path="/unfollow",method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(int entityType,int entityId){
        User user=hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "登录后才能进行关注哦！");
        }
        followService.unfollow(user.getId(),entityType,entityId);

        return CommunityUtil.getJSONString(0, "已取消关注!");
    }

    @RequestMapping(path="/followees/{userId}",method =RequestMethod.GET)
    public String getFollowees(@PathVariable("userId")int userId, Page page, Model model){
        User user=userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user",user);
        page.setLimit(10);
        page.setPath("/followees/"+userId);
        page.setRows((int) followService.findFolloweeCount(userId,CommunityConstant.ENTITY_TYPE_USER));
        List<Map<String,Object>> userList=followService.findFollowees(userId,page.getOffset(),page.getLimit());
        if(userList!=null){
            for(Map<String,Object> map:userList){
                User u=(User)map.get("user");
                map.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);
        return "/site/followee";
    }

    @RequestMapping(path="/followers/{userId}",method =RequestMethod.GET)
    public String getFollowers(@PathVariable("userId")int userId, Page page, Model model){
        User user=userService.findUserById(userId);
        if(user==null){
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user",user);
        page.setLimit(10);
        page.setPath("/followers/"+userId);
        page.setRows((int) followService.findFollowerCount(CommunityConstant.ENTITY_TYPE_USER,userId));
        List<Map<String,Object>> userList=followService.findFollowers(userId,page.getOffset(),page.getLimit());
        if(userList!=null){
            for(Map<String,Object> map:userList){
                User u=(User)map.get("user");
                map.put("hasFollowed",hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users",userList);
        return "/site/follower";
    }

    private boolean hasFollowed(int userId){
        if(hostHolder.getUser()==null){
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(),CommunityConstant.ENTITY_TYPE_USER,userId);
    }
 }
