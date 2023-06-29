//package com.github.maoxp.wx;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.http.ContentType;
//import com.github.maoxp.core.JPayHttpResponse;
//import com.github.maoxp.core.utils.HttpUtil;
//import com.github.maoxp.wx.enums.*;
//import com.github.maoxp.wx.enums.v2.PayApiEnum;
//import com.github.maoxp.wx.utils.PayUtil;
//import com.github.maoxp.wx.utils.WxPayUtil;
//
//import java.io.File;
//import java.io.InputStream;
//import java.security.PrivateKey;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * WxPayApi
// *
// * @author mxp
// * @date 2023/6/15年06月15日 16:50
// * @since JDK 1.8
// */
//public class WxPayApi {
//    WxPayApi() {
//    }
//
//    /**
//     * 获取接口请求的 URL
//     *
//     * @param wxApi {@link IWxApiEnum} 支付 API 接口枚举
//     * @return {@link String} 返回完整的接口请求URL
//     */
//    public static String getReqUrl(IWxApiEnum wxApi) {
//        return getReqUrl(wxApi, null, false);
//    }
//
//    /**
//     * 获取接口请求的 URL
//     *
//     * @param wxApi     {@link IWxApiEnum} 支付 API 接口枚举
//     * @param isSandBox 是否是沙箱环境
//     * @return {@link String} 返回完整的接口请求URL
//     */
//    public static String getReqUrl(IWxApiEnum wxApi, boolean isSandBox) {
//        return getReqUrl(wxApi, null, isSandBox);
//    }
//
//    /**
//     * 获取接口请求的 URL
//     *
//     * @param wxApi     {@link IWxApiEnum} 支付 API 接口枚举
//     * @param wxDomain  {@link IWxDomainEnum} 支付 API 接口域名枚举
//     * @param isSandBox 是否是沙箱环境
//     * @return {@link String} 返回完整的接口请求URL
//     */
//    public static String getReqUrl(IWxApiEnum wxApi, IWxDomainEnum wxDomain, boolean isSandBox) {
//        if (wxDomain == null) {
//            wxDomain = WxDomainEnum.CHINA;
//        }
//        return wxDomain.getDomain()
////                .concat(isSandBox ? PayApiEnum.API_V2_SANDBOX.getUrl() : "")
//                .concat(wxApi.getUrl());
//    }
//
//    /**
//     * 发起请求
//     *
//     * @param apiUrl 接口 URL
//     *               通过 {@link WxPayApi#getReqUrl(IWxApiEnum)}
//     *               或者 {@link WxPayApi#getReqUrl(IWxApiEnum, IWxDomainEnum, boolean)} 来获取
//     * @param params 接口请求参数
//     * @return {@link String} 请求返回的结果
//     */
//    public static String execution(String apiUrl, Map<String, String> params) {
//        return doPost(apiUrl, params);
//    }
//
//    /**
//     * 发起请求
//     *
//     * @param apiUrl 接口 URL
//     *               通过 {@link WxPayApi#getReqUrl(IWxApiEnum)}
//     *               或者 {@link WxPayApi#getReqUrl(IWxApiEnum, IWxDomainEnum, boolean)} 来获取
//     * @param params 接口请求参数
//     * @return {@link String} 请求返回的结果
//     */
//    public static String executionByGet(String apiUrl, Map<String, Object> params) {
//        return doGet(apiUrl, params);
//    }
//
//    /**
//     * 发起请求
//     *
//     * @param apiUrl   接口 URL
//     *                 通过 {@link WxPayApi#getReqUrl(IWxApiEnum)}
//     *                 或者 {@link WxPayApi#getReqUrl(IWxApiEnum, IWxDomainEnum, boolean)} 来获取
//     * @param params   接口请求参数
//     * @param certPath 证书文件路径
//     * @param certPass 证书密码
//     * @return {@link String} 请求返回的结果
//     */
//    public static String execution(String apiUrl, Map<String, String> params, String certPath, String certPass) {
//        return doPostSsl(apiUrl, params, certPath, certPass);
//    }
//
//    /**
//     * 发起请求
//     *
//     * @param apiUrl   接口 URL
//     *                 通过 {@link WxPayApi#getReqUrl(IWxApiEnum)}
//     *                 或者 {@link WxPayApi#getReqUrl(IWxApiEnum, IWxDomainEnum, boolean)} 来获取
//     * @param params   接口请求参数
//     * @param certPath 证书文件路径
//     * @param certPass 证书密码
//     * @param protocol 协议
//     * @return {@link String} 请求返回的结果
//     */
//    public static String executionByProtocol(String apiUrl,
//                                             Map<String, String> params,
//                                             String certPath,
//                                             String certPass,
//                                             String protocol) {
//        return doPostSslByProtocol(apiUrl, params, certPath, certPass, protocol);
//    }
//
//    /**
//     * 发起请求
//     *
//     * @param apiUrl   接口 URL
//     *                 通过 {@link WxPayApi#getReqUrl(IWxApiEnum)}
//     *                 或者 {@link WxPayApi#getReqUrl(IWxApiEnum, IWxDomainEnum, boolean)} 来获取
//     * @param params   接口请求参数
//     * @param certPath 证书文件路径
//     * @return {@link String} 请求返回的结果
//     */
//    public static String execution(String apiUrl, Map<String, String> params, String certPath) {
//        return doPostSsl(apiUrl, params, certPath);
//    }
//
//    /**
//     * 发起请求
//     *
//     * @param apiUrl   接口 URL
//     *                 通过 {@link WxPayApi#getReqUrl(IWxApiEnum)}
//     *                 或者 {@link WxPayApi#getReqUrl(IWxApiEnum, IWxDomainEnum, boolean)} 来获取
//     * @param params   接口请求参数
//     * @param certPath 证书文件路径
//     * @param protocol 协议
//     * @return {@link String} 请求返回的结果
//     */
//    public static String executionByProtocol(String apiUrl, Map<String, String> params, String certPath, String protocol) {
//        return doPostSslByProtocol(apiUrl, params, certPath, protocol);
//    }
//
//    /**
//     * 发起请求
//     *
//     * @param apiUrl   接口 URL
//     *                 通过 {@link WxPayApi#getReqUrl(IWxApiEnum)}
//     *                 或者 {@link WxPayApi#getReqUrl(IWxApiEnum, IWxDomainEnum, boolean)} 来获取
//     * @param params   接口请求参数
//     * @param certFile 证书文件输入流
//     * @param certPass 证书密码
//     * @return {@link String} 请求返回的结果
//     */
//    public static String execution(String apiUrl, Map<String, String> params, InputStream certFile, String certPass) {
//        return doPostSsl(apiUrl, params, certFile, certPass);
//    }
//
//    /**
//     * 发起请求
//     *
//     * @param apiUrl   接口 URL
//     *                 通过 {@link WxPayApi#getReqUrl(IWxApiEnum)}
//     *                 或者 {@link WxPayApi#getReqUrl(IWxApiEnum, IWxDomainEnum, boolean)} 来获取
//     * @param params   接口请求参数
//     * @param certFile 证书文件输入流
//     * @param certPass 证书密码
//     * @param protocol 协议
//     * @return {@link String} 请求返回的结果
//     */
//    public static String executionByProtocol(String apiUrl,
//                                             Map<String, String> params,
//                                             InputStream certFile,
//                                             String certPass,
//                                             String protocol) {
//        return doPostSslByProtocol(apiUrl, params, certFile, certPass, protocol);
//    }
//
//    /**
//     * 发起请求
//     *
//     * @param apiUrl   接口 URL
//     *                 通过 {@link WxPayApi#getReqUrl(IWxApiEnum)}
//     *                 或者 {@link WxPayApi#getReqUrl(IWxApiEnum, IWxDomainEnum, boolean)} 来获取
//     * @param params   接口请求参数
//     * @param certFile 证书文件输入流
//     * @return {@link String} 请求返回的结果
//     */
//    public static String execution(String apiUrl, Map<String, String> params, InputStream certFile) {
//        return doPostSsl(apiUrl, params, certFile);
//    }
//
//    /**
//     * 发起请求
//     *
//     * @param apiUrl   接口 URL
//     *                 通过 {@link WxPayApi#getReqUrl(IWxApiEnum)}
//     *                 或者 {@link WxPayApi#getReqUrl(IWxApiEnum, IWxDomainEnum, boolean)} 来获取
//     * @param params   接口请求参数
//     * @param certFile 证书文件输入流
//     * @param protocol 协议
//     * @return {@link String} 请求返回的结果
//     */
//    public static String executionByProtocol(String apiUrl,
//                                             Map<String, String> params,
//                                             InputStream certFile,
//                                             String protocol) {
//        return doPostSslByProtocol(apiUrl, params, certFile, protocol);
//    }
//
//    public static String execution(String apiUrl,
//                                   Map<String, String> params,
//                                   String certPath,
//                                   String certPass,
//                                   String filePath) {
//        return doUploadSsl(apiUrl, params, certPath, certPass, filePath);
//    }
//
//    public static String executionByProtocol(String apiUrl,
//                                             Map<String, String> params,
//                                             String certPath,
//                                             String certPass,
//                                             String filePath,
//                                             String protocol) {
//        return doUploadSslByProtocol(apiUrl, params, certPath, certPass, filePath, protocol);
//    }
//
//
//    /**
//     * V3 接口统一执行入口
//     *
//     * @param method       {@link RequestMethodEnum} 请求方法
//     * @param urlPrefix    可通过 {@link IWxDomainEnum}来获取
//     * @param urlSuffix    可通过 {@link IWxApiEnum} 来获取，URL挂载参数需要自行拼接
//     * @param mchId        商户Id
//     * @param serialNo     商户 API 证书序列号
//     * @param platSerialNo 平台序列号，接口中包含敏感信息时必传
//     * @param keyPath      apiclient_key.pem 证书路径
//     * @param body         接口请求参数
//     * @param nonceStr     随机字符库
//     * @param timestamp    时间戳
//     * @param authType     认证类型
//     * @param file         文件
//     * @return {@link JPayHttpResponse} 请求返回的结果
//     * @throws Exception 接口执行异常
//     */
//    public static JPayHttpResponse v3(RequestMethodEnum method,
//                                      String urlPrefix,
//                                      String urlSuffix,
//                                      String mchId,
//                                      String serialNo,
//                                      String platSerialNo,
//                                      String keyPath,
//                                      String body,
//                                      String nonceStr,
//                                      long timestamp,
//                                      String authType,
//                                      File file) throws Exception {
//        // 构建 Authorization
//        String authorization = WxPayUtil.buildAuthorization(method, urlSuffix, mchId, serialNo,
//                keyPath, body, nonceStr, timestamp, authType);
//
//        return extractV3getPayHttpResponse(method, urlPrefix, urlSuffix, serialNo, platSerialNo, body, file, authorization);
//    }
//
//    /**
//     * V3 接口统一执行入口
//     *
//     * @param method       {@link RequestMethodEnum} 请求方法
//     * @param urlPrefix    可通过 {@link IWxDomainEnum}来获取
//     * @param urlSuffix    可通过 {@link IWxApiEnum} 来获取，URL挂载参数需要自行拼接
//     * @param mchId        商户Id
//     * @param serialNo     商户 API 证书序列号
//     * @param platSerialNo 平台序列号，接口中包含敏感信息时必传
//     * @param privateKey   商户私钥
//     * @param body         接口请求参数
//     * @param nonceStr     随机字符库
//     * @param timestamp    时间戳
//     * @param authType     认证类型
//     * @param file         文件
//     * @return {@link JPayHttpResponse} 请求返回的结果
//     * @throws Exception 接口执行异常
//     */
//    public static JPayHttpResponse v3(RequestMethodEnum method,
//                                      String urlPrefix,
//                                      String urlSuffix,
//                                      String mchId,
//                                      String serialNo,
//                                      String platSerialNo,
//                                      PrivateKey privateKey,
//                                      String body,
//                                      String nonceStr,
//                                      long timestamp,
//                                      String authType,
//                                      File file) throws Exception {
//        // 构建 Authorization
//        String authorization = WxPayUtil.buildAuthorization(method, urlSuffix, mchId, serialNo,
//                privateKey, body, nonceStr, timestamp, authType);
//
//        return extractV3getPayHttpResponse(method, urlPrefix, urlSuffix, serialNo, platSerialNo, body, file, authorization);
//    }
//
//    private static JPayHttpResponse extractV3getPayHttpResponse(RequestMethodEnum method,
//                                                                String urlPrefix,
//                                                                String urlSuffix,
//                                                                String serialNo,
//                                                                String platSerialNo,
//                                                                String body,
//                                                                File file,
//                                                                String authorization) {
//        if (StrUtil.isEmpty(platSerialNo)) {
//            platSerialNo = serialNo;
//        }
//
//        if (method == RequestMethodEnum.GET) {
//            return get(urlPrefix.concat(urlSuffix), authorization, platSerialNo, null);
//        } else if (method == RequestMethodEnum.POST) {
//            return post(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body);
//        } else if (method == RequestMethodEnum.DELETE) {
//            return delete(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body);
//        } else if (method == RequestMethodEnum.UPLOAD) {
//            return upload(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body, file);
//        } else if (method == RequestMethodEnum.PUT) {
//            return put(urlPrefix.concat(urlSuffix), authorization, platSerialNo, body);
//        }
//        return null;
//    }
//
//    /**
//     * V3 接口统一执行入口
//     *
//     * @param method       {@link RequestMethodEnum} 请求方法
//     * @param urlPrefix    可通过 {@link IWxDomainEnum}来获取
//     * @param urlSuffix    可通过 {@link IWxApiEnum} 来获取，URL挂载参数需要自行拼接
//     * @param mchId        商户Id
//     * @param serialNo     商户 API 证书序列号
//     * @param platSerialNo 平台序列号
//     * @param keyPath      apiclient_key.pem 证书路径
//     * @param body         接口请求参数
//     * @return {@link JPayHttpResponse} 请求返回的结果
//     * @throws Exception 接口执行异常
//     */
//    public static JPayHttpResponse v3(RequestMethodEnum method,
//                                      String urlPrefix,
//                                      String urlSuffix,
//                                      String mchId,
//                                      String serialNo,
//                                      String platSerialNo,
//                                      String keyPath,
//                                      String body) throws Exception {
//        long timestamp = System.currentTimeMillis() / 1000;
//        String authType = AuthTypeEnum.RSA.getUrl();
//        String nonceStr = PayUtil.generateStr();
//        return v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, keyPath, body, nonceStr, timestamp, authType, null);
//    }
//
//    /**
//     * V3 接口统一执行入口
//     *
//     * @param method       {@link RequestMethodEnum} 请求方法
//     * @param urlPrefix    可通过 {@link IWxDomainEnum}来获取
//     * @param urlSuffix    可通过 {@link IWxApiEnum} 来获取，URL挂载参数需要自行拼接
//     * @param mchId        商户Id
//     * @param serialNo     商户 API 证书序列号
//     * @param platSerialNo 平台序列号
//     * @param privateKey   商户私钥
//     * @param body         接口请求参数
//     * @return {@link JPayHttpResponse} 请求返回的结果
//     * @throws Exception 接口执行异常
//     */
//    public static JPayHttpResponse v3(RequestMethodEnum method,
//                                      String urlPrefix,
//                                      String urlSuffix,
//                                      String mchId,
//                                      String serialNo,
//                                      String platSerialNo,
//                                      PrivateKey privateKey,
//                                      String body) throws Exception {
//        long timestamp = System.currentTimeMillis() / 1000;
//        String authType = AuthTypeEnum.RSA.getUrl();
//        String nonceStr = PayUtil.generateStr();
//        return v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, privateKey, body, nonceStr, timestamp, authType, null);
//    }
//
//    /**
//     * V3 接口统一执行入口
//     *
//     * @param method       {@link RequestMethodEnum} 请求方法
//     * @param urlPrefix    可通过 {@link IWxDomainEnum}来获取
//     * @param urlSuffix    可通过 {@link IWxApiEnum} 来获取，URL挂载参数需要自行拼接
//     * @param mchId        商户Id
//     * @param serialNo     商户 API 证书序列号
//     * @param platSerialNo 平台序列号
//     * @param keyPath      apiclient_key.pem 证书路径
//     * @param params       Get 接口请求参数
//     * @return {@link JPayHttpResponse} 请求返回的结果
//     * @throws Exception 接口执行异常
//     */
//    public static JPayHttpResponse v3(RequestMethodEnum method,
//                                      String urlPrefix,
//                                      String urlSuffix,
//                                      String mchId,
//                                      String serialNo,
//                                      String platSerialNo,
//                                      String keyPath,
//                                      Map<String, String> params) throws Exception {
//        long timestamp = System.currentTimeMillis() / 1000;
//        String authType = AuthTypeEnum.RSA.getUrl();
//        String nonceStr = PayUtil.generateStr();
//        if (null != params && !params.keySet().isEmpty()) {
//            urlSuffix = urlSuffix.concat("?").concat(PayUtil.createLinkString(params, true));
//        }
//        return v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, keyPath, "", nonceStr, timestamp, authType, null);
//    }
//
//    /**
//     * V3 接口统一执行入口
//     *
//     * @param method       {@link RequestMethodEnum} 请求方法
//     * @param urlPrefix    可通过 {@link IWxDomainEnum}来获取
//     * @param urlSuffix    可通过 {@link IWxApiEnum} 来获取，URL挂载参数需要自行拼接
//     * @param mchId        商户Id
//     * @param serialNo     商户 API 证书序列号
//     * @param platSerialNo 平台序列号
//     * @param privateKey   商户私钥
//     * @param params       Get 接口请求参数
//     * @return {@link JPayHttpResponse} 请求返回的结果
//     * @throws Exception 接口执行异常
//     */
//    public static JPayHttpResponse v3(RequestMethodEnum method,
//                                      String urlPrefix,
//                                      String urlSuffix,
//                                      String mchId,
//                                      String serialNo,
//                                      String platSerialNo,
//                                      PrivateKey privateKey,
//                                      Map<String, String> params) throws Exception {
//        long timestamp = System.currentTimeMillis() / 1000;
//        String authType = AuthTypeEnum.RSA.getUrl();
//        String nonceStr = PayUtil.generateStr();
//        if (null != params && !params.keySet().isEmpty()) {
//            urlSuffix = urlSuffix.concat("?").concat(PayUtil.createLinkString(params, true));
//        }
//        return v3(method, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, privateKey, "", nonceStr, timestamp, authType, null);
//    }
//
//    /**
//     * V3 接口统一执行入口
//     *
//     * @param urlPrefix    可通过 {@link IWxDomainEnum}来获取
//     * @param urlSuffix    可通过 {@link IWxApiEnum} 来获取，URL挂载参数需要自行拼接
//     * @param mchId        商户Id
//     * @param serialNo     商户 API 证书序列号
//     * @param platSerialNo 平台序列号
//     * @param keyPath      apiclient_key.pem 证书路径
//     * @param body         接口请求参数
//     * @param file         文件
//     * @return {@link JPayHttpResponse} 请求返回的结果
//     * @throws Exception 接口执行异常
//     */
//    public static JPayHttpResponse v3(String urlPrefix,
//                                      String urlSuffix,
//                                      String mchId,
//                                      String serialNo,
//                                      String platSerialNo,
//                                      String keyPath,
//                                      String body,
//                                      File file) throws Exception {
//        long timestamp = System.currentTimeMillis() / 1000;
//        String authType = AuthTypeEnum.RSA.getUrl();
//        String nonceStr = PayUtil.generateStr();
//        return v3(RequestMethodEnum.UPLOAD, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, keyPath, body, nonceStr, timestamp, authType, file);
//    }
//
//    /**
//     * V3 接口统一执行入口
//     *
//     * @param urlPrefix    可通过 {@link IWxDomainEnum}来获取
//     * @param urlSuffix    可通过 {@link IWxApiEnum} 来获取，URL挂载参数需要自行拼接
//     * @param mchId        商户Id
//     * @param serialNo     商户 API 证书序列号
//     * @param platSerialNo 平台序列号
//     * @param privateKey   商户私钥
//     * @param body         接口请求参数
//     * @param file         文件
//     * @return {@link JPayHttpResponse} 请求返回的结果
//     * @throws Exception 接口执行异常
//     */
//    public static JPayHttpResponse v3(String urlPrefix, String urlSuffix, String mchId, String serialNo,
//                                      String platSerialNo, PrivateKey privateKey, String body, File file) throws Exception {
//        long timestamp = System.currentTimeMillis() / 1000;
//        String authType = AuthTypeEnum.RSA.getUrl();
//        String nonceStr = PayUtil.generateStr();
//        return v3(RequestMethodEnum.UPLOAD, urlPrefix, urlSuffix, mchId, serialNo, platSerialNo, privateKey, body, nonceStr, timestamp, authType, file);
//    }
//
//    /**
//     * 获取验签秘钥API
//     *
//     * @param mchId      商户号
//     * @param partnerKey API 密钥
//     * @param signType   签名方式
//     * @return {@link String} 请求返回的结果
//     */
//    public static String getSignKey(String mchId, String partnerKey, SignTypeEnum signType) {
//        Map<String, String> map = new HashMap<>(3);
//        String nonceStr = PayUtil.generateStr();
//        map.put("mch_id", mchId);
//        map.put("nonce_str", nonceStr);
//        map.put("sign", WxPayUtil.createSign(map, partnerKey, signType));
//        return execution(getReqUrl(PayApiEnum.GET_SIGN_KEY), map);
//    }
//
//    /**
//     * @param url    请求url
//     * @param params 请求参数
//     * @return {@link String}    请求返回的结果
//     */
//    public static String doGet(String url, Map<String, Object> params) {
//        return HttpUtil.getDelegate().get(url, params);
//    }
//
//    /**
//     * get 请求
//     *
//     * @param url     请求url
//     * @param params  请求参数
//     * @param headers 请求头
//     * @return {@link JPayHttpResponse}    请求返回的结果
//     */
//    public static JPayHttpResponse get(String url, Map<String, Object> params, Map<String, String> headers) {
//        return HttpUtil.getDelegate().get(url, params, headers);
//    }
//
//    /**
//     * get 请求
//     *
//     * @param url           请求url
//     * @param authorization 授权信息
//     * @param serialNumber  公钥证书序列号
//     * @param params        请求参数
//     * @return {@link JPayHttpResponse}    请求返回的结果
//     */
//    public static JPayHttpResponse get(String url, String authorization, String serialNumber, Map<String, Object> params) {
//        return get(url, params, getHeaders(authorization, serialNumber));
//    }
//
//    /**
//     * post 请求
//     *
//     * @param url     请求url
//     * @param data    请求参数
//     * @param headers 请求头
//     * @return {@link JPayHttpResponse}    请求返回的结果
//     */
//    public static JPayHttpResponse post(String url, String data, Map<String, String> headers) {
//        return HttpUtil.getDelegate().post(url, data, headers);
//    }
//
//    /**
//     * post 请求
//     *
//     * @param url           请求url
//     * @param authorization 授权信息
//     * @param serialNumber  公钥证书序列号
//     * @param data          请求参数
//     * @return {@link JPayHttpResponse}    请求返回的结果
//     */
//    public static JPayHttpResponse post(String url, String authorization, String serialNumber, String data) {
//        return post(url, data, getHeaders(authorization, serialNumber));
//    }
//
//    /**
//     * delete 请求
//     *
//     * @param url     请求url
//     * @param data    请求参数
//     * @param headers 请求头
//     * @return {@link JPayHttpResponse}    请求返回的结果
//     */
//    public static JPayHttpResponse delete(String url, String data, Map<String, String> headers) {
//        return HttpUtil.getDelegate().delete(url, data, headers);
//    }
//
//    /**
//     * delete 请求
//     *
//     * @param url           请求url
//     * @param authorization 授权信息
//     * @param serialNumber  公钥证书序列号
//     * @param data          请求参数
//     * @return {@link JPayHttpResponse}    请求返回的结果
//     */
//    public static JPayHttpResponse delete(String url, String authorization, String serialNumber, String data) {
//        return delete(url, data, getHeaders(authorization, serialNumber));
//    }
//
//    /**
//     * upload 请求
//     *
//     * @param url     请求url
//     * @param params  请求参数
//     * @param headers 请求头
//     * @return {@link JPayHttpResponse}    请求返回的结果
//     */
//    public static JPayHttpResponse upload(String url, Map<String, Object> params, Map<String, String> headers) {
//        return HttpUtil.getDelegate().post(url, params, headers);
//    }
//
//    /**
//     * upload 请求
//     *
//     * @param url           请求url
//     * @param authorization 授权信息
//     * @param serialNumber  公钥证书序列号
//     * @param data          请求参数
//     * @param file          上传文件
//     * @return {@link JPayHttpResponse}    请求返回的结果
//     */
//    public static JPayHttpResponse upload(String url, String authorization, String serialNumber, String data, File file) {
//        Map<String, Object> paramMap = new HashMap<>(2);
//        paramMap.put("file", file);
//        paramMap.put("meta", data);
//        return upload(url, paramMap, getUploadHeaders(authorization, serialNumber));
//    }
//
//
//    /**
//     * put 请求
//     *
//     * @param url     请求url
//     * @param data    请求参数
//     * @param headers 请求头
//     * @return {@link JPayHttpResponse}    请求返回的结果
//     */
//    public static JPayHttpResponse put(String url, String data, Map<String, String> headers) {
//        return HttpUtil.getDelegate().put(url, data, headers);
//    }
//
//    /**
//     * put 请求
//     *
//     * @param url           请求url
//     * @param authorization 授权信息
//     * @param serialNumber  公钥证书序列号
//     * @param data          请求参数
//     * @return {@link JPayHttpResponse}    请求返回的结果
//     */
//    public static JPayHttpResponse put(String url, String authorization, String serialNumber, String data) {
//        return put(url, data, getHeaders(authorization, serialNumber));
//    }
//
//    public static String doPost(String url, Map<String, String> params) {
//        return HttpUtil.getDelegate().post(url, WxPayUtil.toXml(params));
//    }
//
//    public static String doPostSsl(String url, Map<String, String> params, String certPath, String certPass) {
//        return HttpUtil.getDelegate().post(url, WxPayUtil.toXml(params), certPath, certPass);
//    }
//
//    public static String doPostSslByProtocol(String url, Map<String, String> params, String certPath, String certPass, String protocol) {
//        return HttpUtil.getDelegate().post(url, WxPayUtil.toXml(params), certPath, certPass, protocol);
//    }
//
//    public static String doPostSsl(String url, Map<String, String> params, InputStream certFile, String certPass) {
//        return HttpUtil.getDelegate().post(url, WxPayUtil.toXml(params), certFile, certPass);
//    }
//
//    public static String doPostSslByProtocol(String url, Map<String, String> params, InputStream certFile, String certPass, String protocol) {
//        return HttpUtil.getDelegate().post(url, WxPayUtil.toXml(params), certFile, certPass, protocol);
//    }
//
//    public static String doPostSsl(String url, Map<String, String> params, String certPath) {
//        if (params.isEmpty() || !params.containsKey("mch_id")) {
//            throw new RuntimeException("请求参数中必须包含 mch_id，如接口参考中不包 mch_id， 请使用其他同名构造方法。");
//        }
//        String certPass = params.get("mch_id");
//        return doPostSsl(url, params, certPath, certPass);
//    }
//
//    public static String doPostSslByProtocol(String url, Map<String, String> params, String certPath, String protocol) {
//        if (params.isEmpty() || !params.containsKey("mch_id")) {
//            throw new RuntimeException("请求参数中必须包含 mch_id，如接口参考中不包 mch_id， 请使用其他同名构造方法。");
//        }
//        String certPass = params.get("mch_id");
//        return doPostSslByProtocol(url, params, certPath, certPass, protocol);
//    }
//
//    public static String doPostSsl(String url, Map<String, String> params, InputStream certFile) {
//        if (params.isEmpty() || !params.containsKey("mch_id")) {
//            throw new RuntimeException("请求参数中必须包含 mch_id，如接口参考中不包 mch_id， 请使用其他同名构造方法。");
//        }
//        String certPass = params.get("mch_id");
//        return doPostSsl(url, params, certFile, certPass);
//    }
//
//    public static String doPostSslByProtocol(String url, Map<String, String> params, InputStream certFile, String protocol) {
//        if (params.isEmpty() || !params.containsKey("mch_id")) {
//            throw new RuntimeException("请求参数中必须包含 mch_id，如接口参考中不包 mch_id， 请使用其他同名构造方法。");
//        }
//        String certPass = params.get("mch_id");
//        return doPostSslByProtocol(url, params, certFile, certPass, protocol);
//    }
//
//    public static String doUploadSsl(String url, Map<String, String> params, String certPath, String certPass, String filePath) {
//        return HttpUtil.getDelegate().upload(url, WxPayUtil.toXml(params), certPath, certPass, filePath);
//    }
//
//    public static String doUploadSslByProtocol(String url, Map<String, String> params, String certPath, String certPass, String filePath, String protocol) {
//        return HttpUtil.getDelegate().upload(url, WxPayUtil.toXml(params), certPath, certPass, filePath, protocol);
//    }
//
//    public static String doUploadSsl(String url, Map<String, String> params, String certPath, String filePath) {
//        if (params.isEmpty() || !params.containsKey("mch_id")) {
//            throw new RuntimeException("请求参数中必须包含 mch_id，如接口参考中不包 mch_id， 请使用其他同名构造方法。");
//        }
//        String certPass = params.get("mch_id");
//        return doUploadSsl(url, params, certPath, certPass, filePath);
//    }
//
//    public static String doUploadSslByProtocol(String url, Map<String, String> params, String certPath, String filePath, String protocol) {
//        if (params.isEmpty() || !params.containsKey("mch_id")) {
//            throw new RuntimeException("请求参数中必须包含 mch_id，如接口参考中不包 mch_id， 请使用其他同名构造方法。");
//        }
//        String certPass = params.get("mch_id");
//        return doUploadSslByProtocol(url, params, certPath, certPass, filePath, protocol);
//    }
//
//    private static final String OS = System.getProperty("os.name") + "/" + System.getProperty("os.version");
//    private static final String VERSION = System.getProperty("java.version");
//
//
//    public static Map<String, String> getBaseHeaders(String authorization) {
//        String userAgent = String.format(
//                "WeChatPay-IJPay-HttpClient/%s (%s) Java/%s",
//                WxPayApi.class.getPackage().getImplementationVersion(),
//                OS,
//                VERSION == null ? "Unknown" : VERSION);
//
//        Map<String, String> headers = new HashMap<>(5);
//        headers.put("Accept", ContentType.JSON.toString());
//        headers.put("Authorization", authorization);
//        headers.put("User-Agent", userAgent);
//        return headers;
//    }
//
//    public static Map<String, String> getHeaders(String authorization, String serialNumber) {
//        Map<String, String> headers = getBaseHeaders(authorization);
//        headers.put("Content-Type", ContentType.JSON.toString());
//        if (StrUtil.isNotEmpty(serialNumber)) {
//            headers.put("Wechatpay-Serial", serialNumber);
//        }
//        return headers;
//    }
//
//    public static Map<String, String> getUploadHeaders(String authorization, String serialNumber) {
//        Map<String, String> headers = getBaseHeaders(authorization);
//        headers.put("Content-Type", "multipart/form-data;boundary=\"boundary\"");
//        if (StrUtil.isNotEmpty(serialNumber)) {
//            headers.put("Wechatpay-Serial", serialNumber);
//        }
//        return headers;
//    }
//
//    /**
//     * 构建返回参数
//     *
//     * @param response {@link JPayHttpResponse}
//     * @return {@link Map}
//     */
//    public static Map<String, Object> buildResMap(JPayHttpResponse response) {
//        if (response == null) {
//            return null;
//        }
//        Map<String, Object> map = new HashMap<>(6);
//        String timestamp = response.getHeader("Wechatpay-Timestamp");
//        String nonceStr = response.getHeader("Wechatpay-Nonce");
//        String serialNo = response.getHeader("Wechatpay-Serial");   // 微信支付的平台证书序列号
//        String signature = response.getHeader("Wechatpay-Signature");
//        String body = response.getBody();
//        int status = response.getStatus();
//        map.put("timestamp", timestamp);
//        map.put("nonceStr", nonceStr);
//        map.put("serialNumber", serialNo);
//        map.put("signature", signature);
//        map.put("body", body);
//        map.put("status", status);
//        return map;
//    }
//}
