package com.jjynowcoder.community.controller;

import com.jjynowcoder.community.entity.Comment;
import com.jjynowcoder.community.entity.DiscussPost;
import com.jjynowcoder.community.entity.Event;
import com.jjynowcoder.community.event.EventProducer;
import com.jjynowcoder.community.service.CommentService;
import com.jjynowcoder.community.service.DiscussPostService;
import com.jjynowcoder.community.util.CommunityConstant;
import com.jjynowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path="/add/{discussPostId}",method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId")int discussPostId, Comment comment){
       comment.setUserId(hostHolder.getUser().getId());
       comment.setStatus(0);
       comment.setCreateTime(new Date());
       commentService.addComment(comment);
       //触发评论事件
        Event event=new Event().setTopic(TOPIC_COMMENT).setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType()).setEntityId(comment.getEntityId()).setData("postId",discussPostId);
        if(comment.getEntityType()==ENTITY_TYPE_POST){
            DiscussPost target= discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }else if(comment.getEntityType()==ENTITY_TYPE_COMMENT){
            Comment target=commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }
        eventProducer.fireEvent(event);
       return "redirect:/discuss/detail/"+discussPostId;
    }

}
