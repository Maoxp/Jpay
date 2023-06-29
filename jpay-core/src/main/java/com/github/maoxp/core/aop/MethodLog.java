package com.github.maoxp.core.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 打印service的方法从入参到响应的耗时的注解,标记需要切入的方法
 *
 * @author mxp
 * @date 2022年07月14日 15:25
 * @since JDK 1.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodLog {
}
