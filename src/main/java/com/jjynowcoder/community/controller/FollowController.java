package com.jjynowcoder.community.controller;

import com.jjynowcoder.community.entity.User;
import com.jjynowcoder.community.service.FollowService;
import com.jjynowcoder.community.util.CommunityUtil;
import com.jjynowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path="/follow",method = RequestMethod.POST)
    @ResponseBody
    public String follow(int entityType,int entityId){
         User user=hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "登录后才能进行关注哦！");
        }
         followService.follow(user.getId(),entityType,entityId);

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

 }
