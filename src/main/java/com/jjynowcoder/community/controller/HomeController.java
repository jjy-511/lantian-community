package com.jjynowcoder.community.controller;

import com.jjynowcoder.community.entity.DiscussPost;
import com.jjynowcoder.community.entity.Page;
import com.jjynowcoder.community.entity.User;
import com.jjynowcoder.community.service.DiscussPostService;
import com.jjynowcoder.community.service.LikeService;
import com.jjynowcoder.community.service.UserService;
import com.jjynowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;
    @RequestMapping(path="/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        //SpringMVC自动实例化Model与Page，并将Page注入Model
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list=discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        if(list!=null){
            for(DiscussPost d:list){
                Map<String,Object> map=new HashMap<>();
                map.put("post",d);
                User user=userService.findUserById(d.getUserId());
                map.put("user",user);
                long likeCnt=likeService.findEntityLikeCount(ENTITY_TYPE_POST, d.getId());
                map.put("likeCount",likeCnt);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
    return "/index";
    }

    @RequestMapping(path="/error",method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }
}
