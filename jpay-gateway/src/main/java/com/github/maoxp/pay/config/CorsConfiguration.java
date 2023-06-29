package com.github.maoxp.pay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CorsConfiguration
 *
 * @author mxp
 * @date 2023/5/29年05月29日 15:14
 * @since JDK 1.8
 */
@Configuration
public class CorsConfiguration {
    /**
     * 允许跨域请求
     **/
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowCredentials(true);   //带上cookie信息
        config.addAllowedOrigin(org.springframework.web.cors.CorsConfiguration.ALL);  //允许跨域的域名， *表示允许任何域名使用
        config.addAllowedOriginPattern(org.springframework.web.cors.CorsConfiguration.ALL);  //使用addAllowedOriginPattern 避免出现 When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.
        config.addAllowedHeader(org.springframework.web.cors.CorsConfiguration.ALL);   //允许任何请求头
        config.addAllowedMethod(org.springframework.web.cors.CorsConfiguration.ALL);   //允许任何方法（post、get等）
        source.registerCorsConfiguration("/**", config); // CORS 配置对所有接口都有效
        return new CorsFilter(source);
    }

}
