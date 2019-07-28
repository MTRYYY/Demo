package com.stu.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class WebLogAspect {
    //切入点的描述 这是个controller包的切入点
    @Pointcut("execution(public * com.stu.controller..*.*(..))")
    public void controllerLog() {
    }//签名和可以理解层这个切入点的名称

    //在切入点运行之前要干的事情
    @Before("controllerLog()")
    public void before(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();//这个RequestContextHolder是Springmvc提供来获得请求的东西
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 记录下请求内容
        log.info("######################before######################");
        log.info("URL : " + request.getRequestURL().toString());
        log.info("HTTP_METHOD : " + request.getMethod());
        log.info("IP : " + request.getRemoteAddr());
        log.info("THE ARGS OF THE CONTROLLER : " + Arrays.toString(joinPoint.getArgs()));
        //下面这个getSignature().getDeclaringTypeName()是获取包+类名的   然后后面的joinPoint.getSignature.getName()获取了方法名
        log.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("TARGET: " + joinPoint.getTarget());//返回的是需要加强的目标类的对象
        log.info("THIS: " + joinPoint.getThis());//返回的是经过加强后的代理类的对象
        log.info("######################before######################");
    }

    @Around("controllerLog()")
    public Object around(ProceedingJoinPoint joinPoint) {
        long current = System.currentTimeMillis();
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        long end = System.currentTimeMillis();
        log.info("######################Around######################");
        log.info("消耗时间：" + (end - current));
        log.info("######################Around######################");
        return proceed;
    }

    @AfterReturning(returning = "ret", pointcut = "controllerLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        log.info("######################AfterReturning######################");
        log.info("RESPONSE : " + JSONObject.toJSON(ret));
        log.info("######################AfterReturning######################");
    }

    @After("controllerLog()")
    public void after(JoinPoint joinPoint) {
        log.info("######################After######################");
        System.out.println("after");
        log.info("######################After######################");
    }
}
