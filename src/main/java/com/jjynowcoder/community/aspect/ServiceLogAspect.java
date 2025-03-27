package com.jjynowcoder.community.aspect;

import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
public class ServiceLogAspect {

    private static final Logger logger= LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution( *com.jjynowcoder.community.service.*.*(..))")
    public void pointcut(){}

    @Before("pointcut()")
    public void before(JoinPoint joinpoint){
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String ip=request.getRemoteHost();
        String now=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String target= joinpoint.getSignature().getDeclaringTypeName()+"."+joinpoint.getSignature().getName();
        logger.info(String.format("用户[%s],在[%s],访问了[%s]。"),ip,now,target);
    }

}
