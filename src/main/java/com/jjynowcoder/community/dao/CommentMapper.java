package com.jjynowcoder.community.dao;

import com.jjynowcoder.community.entity.Comment;
import com.jjynowcoder.community.service.CommentService;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit);

    int selectCountByEntity(int entityType,int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);
}
