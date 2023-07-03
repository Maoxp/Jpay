package com.github.maoxp.pay.aop;


import com.github.maoxp.core.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class: MethodLogAop
 *
 * @author mxp
 * @date 2022年07月15日 07:41
 * @since JDK 1.8
 */
@Aspect
@Component
@Slf4j
public final class MethodLogAop {

    @Pointcut("@annotation(com.github.maoxp.core.aop.MethodLog)")
    public void methodCachePointcut() { }

    @Around("methodCachePointcut()")
    public Object printServiceResponseDatagram(ProceedingJoinPoint point) throws Throwable {
        long req = System.currentTimeMillis();
        String reqTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        Object[] args = point.getArgs();
        String invokingClass = point.getSignature().getDeclaringTypeName();  // 调用类
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        String invokingMethodName = method.getName();                       // 类的方法名
        String classAndMethodName = invokingClass + "." + invokingMethodName;

        log.info("\n==> Service请求 - [{}]\n ==> 请求时间：{}\n ==> 形参值：{}\n", classAndMethodName, reqTime, JacksonUtil.toJson(args));

        // 执行方法
        Object result = point.proceed();

        String respString = String.valueOf(System.currentTimeMillis() - req);
        log.info("\n==> Service响应 - [{}]\n ==> 耗时：{}s\n ==>  返回参数：{}\n", classAndMethodName, Double.parseDouble(respString) / 1000, JacksonUtil.toJson(result));
        return result;
    }
}
