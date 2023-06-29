package com.github.maoxp.pay.config;

import com.github.maoxp.wx.property.WxPayApiProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 验证属性配置信息是否成功
 *
 * @author mxp
 * @date 2023/6/26年06月26日 08:57
 * @since JDK 1.8
 */
@Component
public class ValidatePropertyConfiguration {
    private final Logger logger = LoggerFactory.getLogger(ValidatePropertyConfiguration.class);

    private final WxPayApiProperty wxPayApiProperty;

    @Autowired
    private ValidatePropertyConfiguration(WxPayApiProperty wxPayApiProperty) {
        this.wxPayApiProperty = wxPayApiProperty;
    }

    @PostConstruct
    public void wxPayApiConfig() {
        if (null == wxPayApiProperty) {
            logger.error("未获取到任何微信支付的配置信息,如有疑问请联系 934861215");
        }
        logger.info("自动注入的微信支付配置信息为 {}", wxPayApiProperty);
    }
}
