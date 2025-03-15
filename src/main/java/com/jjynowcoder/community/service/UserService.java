package com.jjynowcoder.community.service;

import com.jjynowcoder.community.dao.LoginTicketMapper;
import com.jjynowcoder.community.dao.UserMapper;
import com.jjynowcoder.community.entity.LoginTicket;
import com.jjynowcoder.community.entity.User;
import com.jjynowcoder.community.util.CommunityUtil;
import com.jjynowcoder.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static com.jjynowcoder.community.util.CommunityConstant.*;

@Service
public class UserService {
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id){
        return userMapper.selectById(id);
    }

    public Map<String,Object> register(User user){
        Map<String,Object> map=new HashMap<>();
        //空值处理
        if(user==null){
            throw new IllegalArgumentException("传入参数不能为空！");
        }
        if(StringUtils.isBlank(user.getUsername())){
            map.put("usernameMessage","账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(user.getPassword())){
            map.put("PasswordMessage","密码不能为空");
            return map;
        }
        // 长度验证
        if(user.getPassword().length() < 8 ||user.getPassword().length() > 20){
            map.put("passwordMessage", "密码长度应为8~20位");
            return map;
        }
        if(user.getUsername().length() < 2 ||user.getUsername().length() > 15 ){
            map.put("passwordMessage", "账号长度应为2~15位");
            return map;
        }
        if(StringUtils.isBlank(user.getEmail())){
            map.put("emailMessage","邮箱不能为空");
            return map;
        }
        //验证账号是否已存在
        User u = userMapper.selectByName(user.getUsername());
        if(u!=null){
            map.put("usernameMessage","该账号已存在！");
            return map;
        }
        //验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if(u!=null){
            map.put("emailMessage","邮箱已被使用！");
            return map;
        }
        //注册客户
        user.setSalt(CommunityUtil.getRandomString().substring(0,6));
        user.setPassword(CommunityUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.getRandomString());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //发送激活邮件
        Context context=new Context();
        context.setVariable("email",user.getEmail());
        String url =domain +contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);
        String content =templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"您正在注册岚天论坛网，点击以激活",content);
        return map;
    }

    public int activation(int userId,String code){
    User user=userMapper.selectById(userId);
    if(user.getStatus()==1){
        return ACTIVATION_REPEAT;
    }else if(user.getActivationCode().equals(code)){
        userMapper.updateStatus(userId,1);
        return ACTIVATION_SUCCESS;
    }else {
        return ACTIVATION_FAILURE;
    }
    }

    public Map<String,Object> login(String username,String password,int expiredSeconds){
        Map<String,Object> map=new HashMap<>();
        //空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码不能为空");
            return map;
        }

        //验证账号
        User user=userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg","该账号不存在");
            return map;
        }
        if(user.getStatus()==0){
            map.put("usernameMsg","该账号未激活");
            return map;
        }
        password=CommunityUtil.md5(password+user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("passwordMsg","密码错误");
            return map;
        }

        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.getRandomString());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+expiredSeconds* 1000L));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    public void Logout(String ticket){
        loginTicketMapper.updateStatus(ticket,1);
    }

    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId,String headerUrl){
    return userMapper.updateHeader(userId,headerUrl);
    }

    public int updatePassword(int userId,String password){
        return userMapper.updatePassword(userId,password);
    }
}
