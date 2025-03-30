package com.jjynowcoder.community.service;

import com.jjynowcoder.community.dao.CommentMapper;
import com.jjynowcoder.community.entity.Comment;
import com.jjynowcoder.community.util.CommunityConstant;
import com.jjynowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class CommentService implements CommunityConstant {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> findCommentsByEntity(int entityType,int entityId,int offset,int limit){
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    public int findCommentCount(int entityType,int entityId){
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    public Comment findCommentById(int id){
        return commentMapper.selectCommentById(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
       if(comment==null){
           throw new IllegalArgumentException("参数不能为空！");
       }
       comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
       comment.setContent(sensitiveFilter.filter(comment.getContent()));
       int rows=commentMapper.insertComment(comment);
       if(comment.getEntityType()== ENTITY_TYPE_POST){
           int cnt= commentMapper.selectCountByEntity(comment.getEntityType(),comment.getEntityId());
           discussPostService.updateCommentCount(comment.getId(),cnt);
       }
       return rows;
    }

}
