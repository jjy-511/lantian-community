package com.jjynowcoder.community.util;

public class RedisKeyUtil {

    private static final String SPLIT=":";
    private static final String PREFIX_ENTITY_LIKE="like:entity";
    private static final String PREFIX_USER_LIKE="like:user";
    private static final String PREFIX_FOLLOWEE="followee";
    private static final String PREFIX_FOLLOWER="follower";
    private static final String PREFIX_KAPTCHA="kaptcha";
    private static final String PREFIX_TICKET="ticket";
    private static final String PREFIX_USER="user";

    //like:entity:entityType:entityId  ->set(userId)
    public static String getEntityLikeKey(int entityType,int entityId){
         return PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }

    //like:user:userId  ->int
    public static String getUserLikeKey(int userId){
         return PREFIX_USER_LIKE+SPLIT+userId;
    }

    //followee:userId:entityType ->zset(entityId,now)
    public static String getFolloweeKey(int userId,int entityType){
        return PREFIX_FOLLOWEE+SPLIT+userId+SPLIT+entityType;
    }

    //follower:entityType:entityId ->zset(userId,now)
    public static String getFollowerKey(int entityId,int entityType){
        return PREFIX_FOLLOWER+SPLIT+entityType+SPLIT+entityId;
    }

    //登录验证码
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA+SPLIT+owner;
    }

    //登录凭证
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET+SPLIT+ticket;
    }

    public static String getUserKey(int userId){
        return PREFIX_USER+SPLIT+userId;
    }
}
