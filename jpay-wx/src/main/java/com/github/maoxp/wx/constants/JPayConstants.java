package com.github.maoxp.wx.constants;

/**
 * <p>常量</p>
 *
 * @author Javen
 */
public interface JPayConstants {

    /**
     * 证书序列号:固定40字节的字符串
     * @see <a href="https://kf.qq.com/faq/180824JvUZ3i180824YvMNJj.html">API证书及密钥 -> 微信支付商户API证书升级指引（技术人员）</a>
     */
    int SERIAL_NUMBER_LENGTH = 40;

    /**
     * 证书颁发者:权威CA颁发的商户API证书
     */
    String ISSUER = "CN=Tenpay.com Root CA";

    /**
     * 证书CN字段
     */
    String CN = "CN=";

    /**
     * HTTP头部中包括证书序列号
     */
    String WX_CERT_SERIAL_NO = "Wechatpay-Serial";

    /**
     * 处理成功，如果有应答的消息体将返回200
     */
    int CODE_200 = 200;

    /**
     * 已经被成功接受但待处理的请求，将返回202
     */
    int CODE_202 = 202;

    /**
     * 处理成功，若没有应答的消息体将返回204
     */
    int CODE_204 = 204;

    /**
     * 协议或者参数非法
     */
    int CODE_400 = 400;

    /**
     * 签名验证失败
     */
    int CODE_401 = 401;

    /**
     * 权限异常
     */
    int CODE_403 = 403;

    /**
     * 请求的资源不存在
     */
    int CODE_404 = 404;

    /**
     * 请求超过频率限制
     */
    int CODE_429 = 429;

    /**
     * 系统错误
     */
    int CODE_500 = 500;

    /**
     * 服务下线，暂时不可用
     */
    int CODE_502 = 502;

    /**
     * 服务不可用，过载保护
     */
    int CODE_503 = 503;

}
