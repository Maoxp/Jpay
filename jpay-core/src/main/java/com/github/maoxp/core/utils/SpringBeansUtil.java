package com.github.maoxp.core.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * SpringBeansUtil
 *
 * @author mxp
 * @date 2023/6/7年06月07日 16:13
 * @since JDK 1.8
 */
@Component
public class SpringBeansUtil implements ApplicationContextAware {
    /**
     * 上下文对象实例
     */
    @Setter
    @Getter
    private static ApplicationContext context;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBeansUtil.context == null) {
            setContext(applicationContext);
        }
    }

    /** 获取applicationContext */
    public static ApplicationContext getApplicationContext() {
        return context;
    }

    /** 通过name获取 Bean. */
    public static Object getBean(String name) throws BeansException {
        if(!containsBean(name)){
            return null;
        }

        return getApplicationContext().getBean(name);
    }

    /** 通过class获取Bean. */
    public static <T> T getBean(Class<T> clz) throws BeansException {
        try {
            return getApplicationContext().getBean(clz);
        } catch (BeansException e) {
            return null;
        }
    }

    /** 通过name,以及Clazz返回指定的Bean */
    public static <T> T getBean(String name, Class<T> clazz){
        if(!getApplicationContext().containsBean(name)){
            return null;
        }
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name beanName
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return context.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name beanName
     * @return boolean
     * @throws NoSuchBeanDefinitionException
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return context.isSingleton(name);
    }

    /**
     * @param name beanName
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return context.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name beanName
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return context.getAliases(name);
    }

    /**
     * 获取aop代理对象
     *
     * @param invoker 调用者
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker) {
        return (T) AopContext.currentProxy();
    }


}
