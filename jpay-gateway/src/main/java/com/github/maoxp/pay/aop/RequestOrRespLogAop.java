package com.github.maoxp.pay.aop;


import com.github.maoxp.core.utils.JacksonUtil;
import com.github.maoxp.core.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * RequestOrRespLogAop
 *
 * @author mxp
 * @date 2023/6/1年06月01日 15:57
 * @since JDK 1.8
 */
@Aspect
@Component
@Slf4j
public final class RequestOrRespLogAop {
    /**
     * 记录业务请求的时间
     */
    private long req;
    private String reqTime;

    @Pointcut(value = "execution(* com.github.multi.first.project.controller..*.*(..))")
    private void aspectPointcut() {
    }


    @AfterReturning(value = "aspectPointcut()", returning = "rvt")
    public void getControllerBackInfoLog(JoinPoint joinPoint, Object rvt) {
        // 方法执行前的逻辑
        String invokingClassName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String classWithMethod = invokingClassName + "." + joinPoint.getSignature().getName();

        String respString = String.valueOf(System.currentTimeMillis() - this.req);
        log.info("\n<== 响应请求 [" + classWithMethod + "] \n"
                + "<== 请求时间：" + this.reqTime + "\n"
                + "<== 请求耗时：" + Double.parseDouble(respString) / 1000 + "s\n"
                + "<== 响应体：" + JacksonUtil.builder().toJson(rvt)
        );
    }


    /**
     * 获取入参
     *
     * @param joinPoint 切点
     */
    @Before(value = "aspectPointcut()")
    public void getControllerBeforeInfoLog(JoinPoint joinPoint) {
        final HttpServletRequest request = WebUtil.getRequest();

        this.req = System.currentTimeMillis();
        this.reqTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));


        final Enumeration<String> headerNames = request.getHeaderNames();
        final Map<String, String> headerMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            headerMap.put(key, value);
        }
        final String invokingClassName = joinPoint.getSignature().getDeclaringType().getSimpleName();  // 获取被调用类
        final String invokingMethodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();       // 获取被调用的方法
        final String classWithMethod = invokingClassName + "." + invokingMethodName;
        Object[] args = joinPoint.getArgs();        // 获取入参

        String paramBody = "{}";
        if (args != null && args.length != 0) {
            paramBody = JacksonUtil.builder().toJson(args[0]);
        }
        log.info("\n==> 拦截请求 [" + classWithMethod + "]  \n"
                + "==> Ip Address：" + WebUtil.getClientIp(request) + "\n"
                + "==> 请求时间：" + reqTime + "\n"
                + "==> 请求接口：" + request.getMethod() + " " + request.getRequestURL() + "\n"
                + "==> 请求方法：" + classWithMethod + "\n"
                + "==> 请求头：" + headerMap + "\n"
                + "==> 请求内容：" + paramBody + "\n"
        );
    }

}
