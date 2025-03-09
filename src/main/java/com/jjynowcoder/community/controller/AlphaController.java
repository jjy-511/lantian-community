package com.jjynowcoder.community.controller;

import com.jjynowcoder.community.service.AlphaService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Printer;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    private AlphaService alphaService;
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello Spring Boot!";
    }
    @RequestMapping("/data")
    @ResponseBody
    public String find() {
        return alphaService.find();
    }
    @RequestMapping("http")
    public void http(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        //需求获取
        System.out.println(httpServletRequest.getMethod());
        System.out.println(httpServletRequest.getServletPath());
        Enumeration<String> enumeration=httpServletRequest.getHeaderNames();
        while(enumeration.hasMoreElements()){
            String name=enumeration.nextElement();
            String value=httpServletRequest.getHeader(name);
            System.out.println(name+":"+value);
        }
        System.out.println(httpServletRequest.getParameter("shit"));
        //返回相应数据
        httpServletResponse.setContentType("text/html;charset=utf-8");
        try (
                PrintWriter writer = httpServletResponse.getWriter();
                ){
            writer.write("<h1>牛客网By蒋金佑</h1>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //GET请求
    // /student?current=1&limit=20
    @RequestMapping(path ="/students",method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current",required = false,defaultValue = "1") int current,
            @RequestParam(name = "limit",required = false,defaultValue = "10")int limit){
        System.out.println(current);
        System.out.println(limit);
        return "some students";
       }
       // /student/123
    @RequestMapping(path = "/student/{id}",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(
            @PathVariable("id") int id
    ){
        System.out.println(id);
        return "a student";
    }
//Post请求
@RequestMapping(path = "/student",method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name,int number){
    System.out.println(name);
    System.out.println(name);
        return "success";
}
//响应HTML数据
    @RequestMapping(path = "/teacher",method = RequestMethod.GET)
    public ModelAndView getTeacher(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("name","吕帅");
        modelAndView.addObject("age",36);
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }
    @RequestMapping(path = "/school",method =RequestMethod.GET)
    public String getSchool(Model model){
        model.addAttribute("name","吉林大学");
        model.addAttribute("age",76);
        return "/demo/view";
    }
    //响应JSON请求(异步请求)
    //Java对象 -》JSON-》 JS对象
@RequestMapping(path="/emp",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getEmp(){
        Map<String,Object> emp=new HashMap<>();
        emp.put("name","李子阳");
        emp.put("age","21");
        emp.put("salary","2000");
        return emp;
    }
    @RequestMapping(path="/emps",method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String,Object>> getEmps(){
        List<Map<String,Object>> emps=new ArrayList<>();
        Map<String,Object> emp=new HashMap<>();
        emp.put("name","李子阳");
        emp.put("age","21");
        emp.put("salary","2000");
        emps.add(emp);
        emps.add(emp);
        emps.add(emp);
        return emps;
    }
}