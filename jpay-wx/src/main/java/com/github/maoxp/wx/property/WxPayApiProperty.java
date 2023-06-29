package com.github.maoxp.wx.property;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * <p>微信支付常用配置</p>
 *
 * @author maoxp
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class WxPayApiProperty implements Serializable {
    private static final long serialVersionUID = -9044503427692786302L;
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 微信支付商户号
     */
    private String mchId;
    /**
     * 商户平台「API安全」中的 API 密钥,v3 暂未使用到
     */
    private String apiKey;
    /**
     * 商户平台「API安全」中的 APIv3 密钥
     */
    private String apiKeyV3;
    /**
     * 外网访问项目的域名，支付通知、回调中会使用
     */
    private String domain;
    /**
     * 证书绝对路径，v3 暂未使用到，退款 v3 暂未支持需使用 v2 接口
     */
    private String certP12Path;
    /**
     * 商户 key 绝对路径
     */
    private String keyPath;
    /**
     * 商户证书绝对路径
     */
    private String certPath;
    /**
     * 微信平台证书绝对路径，证书需要通过接口获取
     */
    private String platformCertPath;
}
