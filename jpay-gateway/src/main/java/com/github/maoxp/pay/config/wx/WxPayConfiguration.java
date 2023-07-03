package com.github.maoxp.pay.config.wx;

import com.github.maoxp.wx.property.WxPayApiProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * WxPayConfiguration
 * <p>读取微信支付配置</p>
 *
 * @author mxp
 * @date 2023/6/26年06月26日 08:41
 * @since JDK 1.8
 */
@Configuration
public class WxPayConfiguration {

    /**
     * 将属性配置绑定到实例上去
     * @return {@link WxPayApiProperty}
     */
    @Bean
    @ConditionalOnMissingBean(WxPayApiProperty.class)
    @ConfigurationProperties(prefix = "wx.pay")
    public WxPayApiProperty wxPayApiProperty() {
        return new WxPayApiProperty();
    }
}
