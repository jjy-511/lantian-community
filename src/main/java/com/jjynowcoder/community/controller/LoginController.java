package com.jjynowcoder.community.controller;

import com.google.code.kaptcha.Producer;
import com.jjynowcoder.community.entity.User;
import com.jjynowcoder.community.service.UserService;
import com.jjynowcoder.community.util.CommunityConstant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

        private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

        @Autowired
        private UserService userService;

        @Autowired
        private Producer kaptchaProducer;

        @Value("${server.servlet.context-path}")
        private String contextPath;

        @RequestMapping(path ="/register",method = RequestMethod.GET)
        public String getRegisterPage(){
            return "/site/register";
        }

        @RequestMapping(path ="/login",method = RequestMethod.GET)
        public String getLoginPage(){
        return "/site/login";
    }

        @RequestMapping(path ="/register",method =RequestMethod.POST)
        public String register(Model model, User user){
            Map<String,Object> map= userService.register(user);
            if(map==null||map.isEmpty()){
                model.addAttribute("msg","注册成功,已向您的邮箱发送了一封激活邮件，请激活后登录！");
                model.addAttribute("target","/index");
                return "/site/operate-result";
            }else{
                model.addAttribute("usernameMessage",map.get("usernameMessage"));
                model.addAttribute("passwordMessage",map.get("passwordMessage"));
                model.addAttribute("emailMessage",map.get("emailMessage"));
                return "/site/register";
            }

        }

        @RequestMapping(path="/activation/{userId}/{code}",method=RequestMethod.GET)
        public String activation(Model model, @PathVariable("userId")int userId,@PathVariable("code")String code){
            int res=userService.activation(userId,code);
            if(res==ACTIVATION_SUCCESS){
                model.addAttribute("msg","激活成功！您的账号已经可以正常使用啦！");
                model.addAttribute("target","/login");
            }else if(res==ACTIVATION_REPEAT){
                model.addAttribute("msg","账号已激活请勿重复激活。");
                model.addAttribute("target","/index");
            }else{
                model.addAttribute("msg","激活失败，您提供的激活码不正确。");
                model.addAttribute("target","/index");
            }
            return "/site/operate-result";
        }

        @RequestMapping(path="/kaptcha",method = RequestMethod.GET)
        public void getKaptcha(HttpServletResponse response, HttpSession session){
            //生成验证码
            String text = kaptchaProducer.createText();
            BufferedImage image = kaptchaProducer.createImage(text);

            //验证码存入session
            session.setAttribute("kaptcha",text);

            //图片输出给浏览器
            response.setContentType("image/png");
            try {
                OutputStream os=response.getOutputStream();
                ImageIO.write(image,"png",os);
            } catch (IOException e) {
                logger.error("响应验证码失败："+e.getMessage());
            }
        }

        @RequestMapping(path="/login",method =RequestMethod.POST)
        public String login(String username,String password,String code,boolean remember,
                            Model model,HttpSession session,HttpServletResponse response){
            String kaptcha =(String)session.getAttribute("kaptcha");
            if(StringUtils.isBlank(kaptcha)||StringUtils.isBlank(code)||!kaptcha.equalsIgnoreCase(code)){
                model.addAttribute("codeMsg","验证码错误");
                return "/site/login";
            }
            int expiredSeconds=remember?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
            Map<String,Object> map= userService.login(username,password,expiredSeconds);
            if(map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath(contextPath);
                cookie.setMaxAge(expiredSeconds);
                response.addCookie(cookie);
                return "redirect:/index";
            }else{
                model.addAttribute("usernameMsg",map.get("usernameMsg"));
                model.addAttribute("passwordMsg",map.get("passwordMsg"));
                return "/site/login";
            }
        }

        @RequestMapping(path = "/logout",method = RequestMethod.GET)
        public String logout(@CookieValue("ticket") String ticket){
        userService.Logout(ticket);
        return "redirect:/login";
        }
}
