package com.github.maoxp.pay.config.wx;

import com.github.maoxp.wx.property.WxPayApiProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 验证属性配置信息是否成功
 *
 * @author mxp
 * @date 2023/6/26年06月26日 08:57
 * @since JDK 1.8
 */
@Configuration
@Slf4j
public class ValidatePropertyConfiguration {

    private final WxPayApiProperty wxPayApiProperty;

    @Autowired
    public ValidatePropertyConfiguration(WxPayApiProperty wxPayApiProperty) {
        this.wxPayApiProperty = wxPayApiProperty;
    }


    @PostConstruct
    public void wxPayApiConfig() {

        if (null == wxPayApiProperty) {
            log.error("未获取到任何微信支付的配置信息,如有疑问请联系 934861215");
        }
        log.info("自动注入的微信支付配置信息为 {}", wxPayApiProperty);
    }
}
