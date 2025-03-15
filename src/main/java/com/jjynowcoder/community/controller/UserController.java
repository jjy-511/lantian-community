package com.jjynowcoder.community.controller;

import com.jjynowcoder.community.entity.User;
import com.jjynowcoder.community.service.UserService;
import com.jjynowcoder.community.util.CommunityUtil;
import com.jjynowcoder.community.util.HostHolder;
import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger= LoggerFactory.getLogger(UserController.class);

    @Value("${coumunity.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage==null){
            model.addAttribute("error","您还没有上传图片！");
            return "/site/setting";
        }
        String filename= headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(!(suffix.equals(".jpg") || suffix.equals(".jpeg") || suffix.equals(".png"))){
            model.addAttribute("error","不支持的文件类型！");
            return "/site/setting";
        }
        //生成随机文件名
        filename = CommunityUtil.getRandomString()+suffix;
        File des=new File(uploadPath+"/"+filename);
        try {
            //存储文件
            headerImage.transferTo(des);
        } catch (IOException e) {
            logger.error("上传文件失败："+e.getMessage());
            throw new RuntimeException("上传文件失败，服务器发生异常！",e);
        }
        User user=hostHolder.getUser();
        String headUrl = domain+contextPath+"/user/header/"+filename;
        userService.updateHeader(user.getId(),headUrl);
        return "redirect:/index";
    }

    @RequestMapping(path="/header/{filename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename")String filename, HttpServletResponse response){
        //服务器存放路径
        filename= uploadPath+"/"+filename;
        //文件后缀解析
        String suffix = filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/"+suffix);
        try (
                OutputStream os= response.getOutputStream();
                FileInputStream fis=new FileInputStream(filename);
                ){
            byte[] buffer =new byte[1024];
            int i=0;
            while((i=fis.read(buffer))!=-1){
                os.write(buffer,0,i);
            }
        } catch (IOException e) {
            logger.error("读取头像失败"+e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
