//package com.github.multi.first.project.bean;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.servlet.Filter;
//import javax.servlet.Servlet;
//import javax.sql.DataSource;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * DruidConfig
// *
// * @author mxp
// * @date 2023/6/14年06月14日 11:12
// * @since JDK 1.8
// */
//@Configuration
//public class DruidConfig {
//    @ConfigurationProperties(prefix = "spring.datasource")
//    @Bean
//    public DataSource druidDatasource() {
//        return new DruidDataSource();
//    }
//
//    /**
//     * 1.配置 Druid 监控管理后台的Servlet, 内置 Servlet 容器时没有web.xml文件，所以使用 Spring Boot 的注册 Servlet 方式
//     *
//     * @return ServletRegistrationBean
//     */
//    @Bean
//    @ConditionalOnMissingBean
//    public ServletRegistrationBean<Servlet> statViewServlet() {
//        ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
//        // 这些参数可以在 com.alibaba.druid.support.http.StatViewServlet 的父类 com.alibaba.druid.support.http.ResourceServlet 中找到
//        Map<String, String> initParams = new HashMap<>();
//
//        //登录查看信息的账号密码, 用于登录Druid监控后台
//        initParams.put("loginUsername", "admin");
//        initParams.put("loginPassword", "123456");
//        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
//        initParams.put("deny", "192.168.10.132");
//        //白名单, 默认允许所有访问
//        initParams.put("allow", "");
//        //是否可以重置数据
//        initParams.put("resetEnable", "false");
//
//        servletRegistrationBean.setInitParameters(initParams);
//        return servletRegistrationBean;
//    }
//
//    /**
//     * 2.配置web监控的filter
//     *
//     * @return FilterRegistrationBean
//     */
//    @Bean
//    public FilterRegistrationBean<Filter> webStatFilter() {
//        Map<String, String> initParams = new HashMap<>();
//        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//
//        //创建过滤器
//        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
//        bean.setFilter(new WebStatFilter());
//        //过滤路径
//        bean.setUrlPatterns(Collections.singletonList("/*"));
//        //忽略资源
//        bean.setInitParameters(initParams);
//        return bean;
//    }
//}
