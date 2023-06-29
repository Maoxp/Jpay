package com.github.maoxp.wx;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.github.maoxp.core.JPayHttpResponse;
import com.github.maoxp.core.utils.HttpUtil;
import com.github.maoxp.wx.enums.AuthTypeEnum;
import com.github.maoxp.wx.enums.IWxApiEnum;
import com.github.maoxp.wx.enums.RequestMethodEnum;
import com.github.maoxp.wx.enums.WxDomainEnum;
import com.github.maoxp.wx.utils.PayUtil;
import com.github.maoxp.wx.utils.WxPayUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * WxApi
 *
 * @author mxp
 * @date 2023/6/27年06月27日 10:32
 * @since JDK 1.8
 */
@Data
public final class WxApi {
    private static String domainUrl;

    public static WxApi domain(WxDomainEnum wxDomainEnum) {
        domainUrl = wxDomainEnum.getDomain();
        return new WxApi();
    }

    /**
     * V3 接口统一执行入口
     *
     * @param method       {@link RequestMethodEnum} 请求方法
     * @param urlSuffix    可通过 {@link IWxApiEnum} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户号Id
     * @param serialNo     商户API证书序列号
     * @param platSerialNo 微信支付平台序列号，接口中包含敏感信息时必传
     * @param keyPath      商户私钥
     * @param body         接口请求参数
     * @return {@link JPayHttpResponse} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public JPayHttpResponse v3(RequestMethodEnum method,
                               String urlSuffix,
                               String mchId,
                               String serialNo,
                               String platSerialNo,
                               String keyPath,
                               String body) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        String authType = AuthTypeEnum.RSA.getUrl();
        String nonceStr = PayUtil.generateStr();

        // 构建 Authorization
        String authorization = WxPayUtil.buildAuthorization(method, urlSuffix, mchId, serialNo, keyPath, body, nonceStr, timestamp, authType);
        if (StrUtil.isEmpty(platSerialNo)) {
            platSerialNo = serialNo;
        }

        if (method == RequestMethodEnum.GET) {
            return InternalClient.get(domainUrl.concat(urlSuffix), null, authorization, platSerialNo);
        } else if (method == RequestMethodEnum.POST) {
            return InternalClient.post(domainUrl.concat(urlSuffix), body, authorization, platSerialNo);
        }

        return null;
    }

    /**
     * @param method       {@link RequestMethodEnum} 请求方法
     * @param urlSuffix    可通过 {@link IWxApiEnum} 来获取，URL挂载参数需要自行拼接
     * @param mchId        商户号Id
     * @param serialNo     商户API证书序列号
     * @param platSerialNo 微信支付平台序列号，接口中包含敏感信息时必传
     * @param keyPath      商户私钥
     * @param params       Get 接口请求参数
     * @return {@link JPayHttpResponse} 请求返回的结果
     * @throws Exception 接口执行异常
     */
    public JPayHttpResponse v3(RequestMethodEnum method,
                               String urlSuffix,
                               String mchId,
                               String serialNo,
                               String platSerialNo,
                               String keyPath,
                               Map<String, String> params) throws Exception {
        if (null != params && !params.keySet().isEmpty()) {
            urlSuffix = urlSuffix.concat("?").concat(PayUtil.createLinkString(params, true));
        }

        return v3(method, urlSuffix, mchId, serialNo, platSerialNo, keyPath, "");
    }


    private static class InternalClient {
        private static final String OS = System.getProperty("os.name") + "/" + System.getProperty("os.version");
        private static final String VERSION = System.getProperty("java.version");

        /**
         * get 请求
         *
         * @param url           请求url
         * @param params        请求参数
         * @param authorization 授权信息
         * @param serialNo      公钥证书序列号
         * @return {@link JPayHttpResponse}    请求返回的结果
         */
        public static JPayHttpResponse get(String url, Map<String, Object> params, String authorization, String serialNo) {
            return HttpUtil.getDelegate().get(url, params, getHeaders(authorization, serialNo));
        }


        /**
         * post 请求
         *
         * @param url           请求url
         * @param data          请求参数
         * @param authorization 授权信息
         * @param serialNumber  公钥证书序列号
         * @return {@link JPayHttpResponse}    请求返回的结果
         */
        public static JPayHttpResponse post(String url, String data, String authorization, String serialNumber) {
            return HttpUtil.getDelegate().post(url, data, getHeaders(authorization, serialNumber));
        }

        /**
         * v3整合的请求头
         *
         * @param authorization 授权信息
         * @param serialNo      公钥证书序列号
         * @return header
         */
        private static Map<String, String> getHeaders(String authorization, String serialNo) {
            Map<String, String> headers = getBaseHeaders(authorization);
            // V3所有的API请求必须使用 Content-Type: application/json, 注意：图片｜视频上传API除外。
            headers.put("Content-Type", ContentType.JSON.toString());
            if (StrUtil.isNotEmpty(serialNo)) {
                // 证书序列号包含在请求HTTP头部的 Wechatpay-Serial
                headers.put("Wechatpay-Serial", serialNo);
            }
            return headers;
        }

        private static Map<String, String> getBaseHeaders(String authorization) {
            String userAgent = String.format("WeChatPay-JPay-HttpClient/%s (%s) Java/%s",
                    WxApi.class.getPackage().getImplementationVersion(),
                    OS,
                    null == VERSION ? "Unknown" : VERSION);

            Map<String, String> headers = new HashMap<>(5);
            headers.put("Accept", ContentType.JSON.toString());
            headers.put("Authorization", authorization);
            headers.put("User-Agent", userAgent);
            headers.put("Accept-Language", Header.ACCEPT_LANGUAGE.toString());
            return headers;
        }
    }
}
