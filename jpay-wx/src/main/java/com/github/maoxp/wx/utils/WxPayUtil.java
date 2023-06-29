package com.github.maoxp.wx.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.maoxp.core.JPayHttpResponse;
import com.github.maoxp.wx.constants.JPayConstants;
import com.github.maoxp.wx.enums.RequestMethodEnum;
import com.github.maoxp.wx.enums.SignTypeEnum;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>微信支付工具类</p>
 *
 * @author Javen
 */
@Slf4j
public class WxPayUtil {
    private static final String FIELD_SIGN = "sign";
    private static final String FIELD_SIGN_TYPE = "sign_type";


    /**
     * 支付异步通知时校验 sign
     *
     * @param params     参数
     * @param partnerKey 支付密钥
     * @return {boolean}
     */
    public static boolean verifyNotify(Map<String, String> params, String partnerKey) {
        String sign = params.get(FIELD_SIGN);
        String localSign = createSign(params, partnerKey, SignTypeEnum.MD5);
        return sign.equals(localSign);
    }

    /**
     * 支付异步通知时校验 sign
     *
     * @param params     参数
     * @param partnerKey 支付密钥
     * @param signType   签名类型
     * @param signKey    签名字符
     * @return {boolean}
     */
    public static boolean verifyNotify(Map<String, String> params,
                                       String partnerKey,
                                       SignTypeEnum signType,
                                       String signKey) {
        if (CharSequenceUtil.isEmpty(signKey)) {
            signKey = FIELD_SIGN;
        }
        String sign = params.get(signKey);
        String localSign = createSign(params, partnerKey, signType, signKey);
        return sign.equals(localSign);
    }

    /**
     * 支付异步通知时校验 sign
     *
     * @param params     参数
     * @param partnerKey 支付密钥
     * @param signKey    签名字符
     * @return {boolean}
     */
    public static boolean verifyNotify(Map<String, String> params, String partnerKey, String signKey) {
        return verifyNotify(params, partnerKey, SignTypeEnum.MD5, signKey);
    }

    /**
     * 支付异步通知时校验 sign
     *
     * @param params     参数
     * @param partnerKey 支付密钥
     * @param signType   {@link SignTypeEnum}
     * @return {@link Boolean} 验证签名结果
     */
    public static boolean verifyNotify(Map<String, String> params, String partnerKey, SignTypeEnum signType) {
        return verifyNotify(params, partnerKey, signType, null);
    }

    /**
     * 生成签名
     *
     * @param params     需要签名的参数
     * @param partnerKey 密钥
     * @param signType   签名类型
     * @return 签名后的数据
     */
    public static String createSign(Map<String, String> params, String partnerKey, SignTypeEnum signType) {
        return createSign(params, partnerKey, signType, null);
    }

    /**
     * 生成签名
     *
     * @param params     需要签名的参数
     * @param partnerKey 商户平台设置的密钥key，微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->设置API密钥
     * @param signType   签名类型
     * @param signKey    签名字符
     * @return 签名后的数据
     */
    public static String createSign(Map<String, String> params,
                                    String partnerKey,
                                    SignTypeEnum signType,
                                    String signKey) {
        if (signType == null) {
            signType = SignTypeEnum.MD5;
        }
        if (CharSequenceUtil.isEmpty(signKey)) {
            signKey = FIELD_SIGN;
        }
        // 生成签名前先去除sign
        params.remove(signKey);

        String stringA = PayUtil.createLinkString(params);
        String stringSignTemp = stringA + "&key=" + partnerKey;
        if (signType == SignTypeEnum.MD5) {
            return PayUtil.md5(stringSignTemp).toUpperCase();
        } else {
            return PayUtil.hmacSha256(stringSignTemp, partnerKey).toUpperCase();
        }
    }

    /**
     * 生成签名
     *
     * @param params 需要签名的参数
     * @param secret 企业微信支付应用secret
     * @return 签名后的数据
     */
    public static String createSign(Map<String, String> params, String secret) {
        // 生成签名前先去除sign
        params.remove(FIELD_SIGN);
        String tempStr = PayUtil.createLinkString(params);
        String stringSignTemp = tempStr + "&secret=" + secret;
        return PayUtil.md5(stringSignTemp).toUpperCase();
    }

    /**
     * 构建签名
     *
     * @param params     需要签名的参数
     * @param partnerKey 密钥
     * @param signType   签名类型
     * @return 签名后的 Map
     */
    public static Map<String, String> buildSign(Map<String, String> params, String partnerKey, SignTypeEnum signType) {
        return buildSign(params, partnerKey, signType, true);
    }

    /**
     * 构建签名
     *
     * @param params       需要签名的参数
     * @param partnerKey   密钥
     * @param signType     签名类型
     * @param haveSignType 签名是否包含 sign_type 字段
     * @return 签名后的 Map
     */
    public static Map<String, String> buildSign(Map<String, String> params,
                                                String partnerKey,
                                                SignTypeEnum signType,
                                                boolean haveSignType) {
        return buildSign(params, partnerKey, signType, null, null, haveSignType);
    }

    /**
     * 构建签名
     *
     * @param params       需要签名的参数
     * @param partnerKey   密钥
     * @param signType     签名类型
     * @param signKey      签名字符串
     * @param signTypeKey  签名类型字符串
     * @param haveSignType 签名是否包含签名类型字符串
     * @return 签名后的 Map
     */
    public static Map<String, String> buildSign(Map<String, String> params,
                                                String partnerKey,
                                                SignTypeEnum signType,
                                                String signKey,
                                                String signTypeKey,
                                                boolean haveSignType) {
        if (CharSequenceUtil.isEmpty(signKey)) {
            signKey = FIELD_SIGN;
        }
        if (haveSignType) {
            if (CharSequenceUtil.isEmpty(signTypeKey)) {
                signTypeKey = FIELD_SIGN_TYPE;
            }
            params.put(signTypeKey, signType.getType());
        }
        String sign = createSign(params, partnerKey, signType);
        params.put(signKey, sign);
        return params;
    }

//
//    /**
//     * <p>生成二维码链接</p>
//     * <p>原生支付接口模式一(扫码模式一)</p>
//     *
//     * @param sign      签名
//     * @param appId     公众账号ID
//     * @param mchId     商户号
//     * @param productId 商品ID
//     * @param timeStamp 时间戳
//     * @param nonceStr  随机字符串
//     * @return {String}
//     */
//    public static String bizPayUrl(String sign, String appId, String mchId, String productId, String timeStamp, String nonceStr) {
//        String rules = "weixin://wxpay/bizpayurl?sign=Temp&appid=Temp&mch_id=Temp&product_id=Temp&time_stamp=Temp&nonce_str=Temp";
//        return replace(rules, "Temp", sign, appId, mchId, productId, timeStamp, nonceStr);
//    }
//
//    /**
//     * <p>生成二维码链接</p>
//     * <p>原生支付接口模式一(扫码模式一)</p>
//     *
//     * @param partnerKey 密钥
//     * @param appId      公众账号ID
//     * @param mchId      商户号
//     * @param productId  商品ID
//     * @param timeStamp  时间戳
//     * @param nonceStr   随机字符串
//     * @param signType   签名类型
//     * @return {String}
//     */
//    public static String bizPayUrl(String partnerKey, String appId, String mchId, String productId, String timeStamp, String nonceStr, SignTypeEnum signType) {
//        HashMap<String, String> map = new HashMap<>(5);
//        map.put("appid", appId);
//        map.put("mch_id", mchId);
//        map.put("time_stamp", StrUtil.isEmpty(timeStamp) ? Long.toString(System.currentTimeMillis() / 1000) : timeStamp);
//        map.put("nonce_str", StrUtil.isEmpty(nonceStr) ? PayUtil.generateStr() : nonceStr);
//        map.put("product_id", productId);
//        return bizPayUrl(createSign(map, partnerKey, signType), appId, mchId, productId, timeStamp, nonceStr);
//    }
//
//    /**
//     * <p>生成二维码链接</p>
//     * <p>原生支付接口模式一(扫码模式一)</p>
//     *
//     * @param partnerKey 密钥
//     * @param appId      公众账号ID
//     * @param mchId      商户号
//     * @param productId  商品ID
//     * @return {String}
//     */
//    public static String bizPayUrl(String partnerKey, String appId, String mchId, String productId) {
//        String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
//        String nonceStr = PayUtil.generateStr();
//        HashMap<String, String> map = new HashMap<>(5);
//        map.put("appid", appId);
//        map.put("mch_id", mchId);
//        map.put("time_stamp", timeStamp);
//        map.put("nonce_str", nonceStr);
//        map.put("product_id", productId);
//        return bizPayUrl(createSign(map, partnerKey, null), appId, mchId, productId, timeStamp, nonceStr);
//    }
//
//
//    /**
//     * 替换url中的参数
//     *
//     * @param str   原始字符串
//     * @param regex 表达式
//     * @param args  替换字符串
//     * @return {String}
//     */
//    public static String replace(String str, String regex, String... args) {
//        for (String arg : args) {
//            str = str.replaceFirst(regex, arg);
//        }
//        return str;
//    }

    /**
     * 判断接口返回的 code
     *
     * @param codeValue code 值
     * @return 是否是 SUCCESS
     */
    public static boolean codeIsOk(String codeValue) {
        return StrUtil.isNotEmpty(codeValue) && "SUCCESS".equals(codeValue);
    }
//
//    /**
//     * <p>公众号支付-预付订单再次签名</p>
//     * <p>注意此处签名方式需与统一下单的签名类型一致</p>
//     *
//     * @param prepayId   预付订单号
//     * @param appId      应用编号
//     * @param partnerKey API Key
//     * @param signType   签名方式
//     * @return 再次签名后的 Map
//     */
//    public static Map<String, String> prepayIdCreateSign(String prepayId, String appId, String partnerKey, SignTypeEnum signType) {
//        Map<String, String> packageParams = new HashMap<>(6);
//        packageParams.put("appId", appId);
//        packageParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
//        packageParams.put("nonceStr", String.valueOf(System.currentTimeMillis()));
//        packageParams.put("package", "prepay_id=" + prepayId);
//        if (signType == null) {
//            signType = SignTypeEnum.MD5;
//        }
//        packageParams.put("signType", signType.getType());
//        String packageSign = WxPayUtil.createSign(packageParams, partnerKey, signType);
//        packageParams.put("paySign", packageSign);
//        return packageParams;
//    }
//
    /**
     * JS 调起支付签名
     *
     * @param appId    应用编号
     * @param prepayId 预付订单号
     * @param keyPath  key.pem 证书路径（商户私钥）
     * @return 唤起支付需要的参数
     * @throws Exception 错误信息
     */
    public static Map<String, String> jsApiCreateSign(String appId, String prepayId, String keyPath) throws Exception {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = String.valueOf(System.currentTimeMillis());
        String packageStr = "prepay_id=" + prepayId;
        Map<String, String> packageParams = new HashMap<>(6);
        packageParams.put("appId", appId);
        packageParams.put("timeStamp", timeStamp);
        packageParams.put("nonceStr", nonceStr);
        packageParams.put("package", packageStr);
        packageParams.put("signType", SignTypeEnum.RSA.toString());
        ArrayList<String> list = new ArrayList<>();
        list.add(appId);
        list.add(timeStamp);
        list.add(nonceStr);
        list.add(packageStr);
        String packageSign = PayUtil.createSignByV3(
                PayUtil.buildSignMessage(list),
                keyPath
        );
        packageParams.put("paySign", packageSign);
        return packageParams;
    }

    /**
     * v2 APP 支付-预付订单再次签名
     * <p>注意此处签名方式需与统一下单的签名类型一致</p>
     *
     * @param appId      应用编号
     * @param partnerId  商户号
     * @param prepayId   预付订单号
     * @param partnerKey API Key
     * @param signType   签名方式
     * @return 再次签名后的 Map
     */
    public static Map<String, String> appPrepayIdCreateSign(String appId, String partnerId, String prepayId, String partnerKey, SignTypeEnum signType) {
        Map<String, String> packageParams = new HashMap<>(8);
        packageParams.put("appid", appId);
        packageParams.put("partnerid", partnerId);
        packageParams.put("prepayid", prepayId);
        packageParams.put("package", "Sign=WXPay");
        packageParams.put("noncestr", String.valueOf(System.currentTimeMillis()));
        packageParams.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        if (signType == null) {
            signType = SignTypeEnum.MD5;
        }
        String packageSign = createSign(packageParams, partnerKey, signType);
        packageParams.put("sign", packageSign);
        return packageParams;
    }


    /**
     * <p>v2 小程序-预付订单再次签名</p>
     * <p>注意此处签名方式需与统一下单的签名类型一致</p>
     *
     * @param appId      应用编号
     * @param prepayId   预付订单号
     * @param partnerKey API Key
     * @param signType   签名方式
     * @return 再次签名后的 Map
     */
    public static Map<String, String> miniAppPrepayIdCreateSign(String appId, String prepayId, String partnerKey, SignTypeEnum signType) {
        Map<String, String> packageParams = new HashMap<>(6);
        packageParams.put("appId", appId);
        packageParams.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        packageParams.put("nonceStr", String.valueOf(System.currentTimeMillis()));
        packageParams.put("package", "prepay_id=" + prepayId);
        if (signType == null) {
            signType = SignTypeEnum.MD5;
        }
        packageParams.put("signType", signType.getType());
        String packageSign = createSign(packageParams, partnerKey, signType);
        packageParams.put("paySign", packageSign);
        return packageParams;
    }


    /**
     * App 调起支付签名
     *
     * @param appId     应用编号
     * @param partnerId 商户编号
     * @param prepayId  预付订单号
     * @param keyPath   key.pem 证书路径
     * @return 唤起支付需要的参数
     * @throws Exception 错误信息
     */
    public static Map<String, String> appCreateSign(String appId, String partnerId, String prepayId, String keyPath) throws Exception {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = String.valueOf(System.currentTimeMillis());
        Map<String, String> packageParams = new HashMap<>(8);
        packageParams.put("appid", appId);
        packageParams.put("partnerid", partnerId);
        packageParams.put("prepayid", prepayId);
        packageParams.put("package", "Sign=WXPay");
        packageParams.put("timestamp", timeStamp);
        packageParams.put("noncestr", nonceStr);
        packageParams.put("signType", SignTypeEnum.RSA.toString());
        ArrayList<String> list = new ArrayList<>();
        list.add(appId);
        list.add(timeStamp);
        list.add(nonceStr);
        list.add(prepayId);
        String packageSign = PayUtil.createSignByV3(
                PayUtil.buildSignMessage(list),
                keyPath
        );
        packageParams.put("sign", packageSign);
        return packageParams;
    }


    /**
     * v3 签名生成
     *
     * @param method    {@link RequestMethodEnum} 请求方法
     * @param urlSuffix URL挂载参数需要自行拼接
     * @param mchId     微信商户号
     * @param serialNo  商户 API 证书序列号
     * @param keyPath   商户证书绝对路径
     * @param body      接口请求参数
     * @param nonceStr  随机字符库
     * @param timestamp 时间戳
     * @param authType  认证（算法）类型
     * @return {@link String} 返回 v3 所需的 Authorization
     * @throws Exception 异常信息
     */
    public static String buildAuthorization(RequestMethodEnum method,
                                            String urlSuffix,
                                            String mchId,
                                            String serialNo,
                                            String keyPath,
                                            String body,
                                            String nonceStr,
                                            long timestamp,
                                            String authType) throws Exception {
        // 构造签名串(签名串一共有五行)
        String buildSignMessage = PayUtil.buildSignMessage(method, urlSuffix, timestamp, nonceStr, body);
        // 计算签名值（使用商户私钥对待签名串进行SHA256 with RSA签名，并对签名结果进行Base64编码得到签名值。）
        String signature = PayUtil.createSignByV3(buildSignMessage, keyPath);
        // 根据平台规则生成请求头（Authorization: 认证类型 签名信息）
        return PayUtil.getAuthorization(mchId, serialNo, nonceStr, String.valueOf(timestamp), signature, authType);
    }


    /**
     * V3  签名验证
     * <br>
     * 如果验证商户的请求签名正确，微信支付会在应答的HTTP头部中包括应答签名。我们建议商户验证应答签名。
     * <br>
     * 同样的，微信支付会在回调的HTTP头部中包括回调报文的签名。商户必须 验证回调的签名，以确保回调是由微信支付发送。
     *
     * @param response         接口请求返回的 {@link JPayHttpResponse}
     * @param platformCertPath 平台证书路径
     * @return 签名结果
     * @throws Exception 异常信息
     */
    public static boolean verifySignature(JPayHttpResponse response, String platformCertPath) throws Exception {
        String timestamp = response.getHeader("Wechatpay-Timestamp");
        String nonceStr = response.getHeader("Wechatpay-Nonce");
        String signature = response.getHeader("Wechatpay-Signature");
        String serialNo = response.getHeader(JPayConstants.WX_CERT_SERIAL_NO);
        String body = response.getBody();

        InputStream inputStream = PayUtil.getCertFileInputStream(platformCertPath);
        // 获取平台证书序列号
        final X509Certificate x509Certificate = PayUtil.getX509Certificate(inputStream);
        ;
        final String serialNUmber = x509Certificate.getSerialNumber().toString(16).toUpperCase();
        // 验证证书序列号
        if (!StrUtil.equals(serialNUmber, serialNo)) {
            log.error("V3 签名验证失败（当前用户持有的微信平台证书序列号 与 应答微信支付的平台证书序列号 不一致）");
            throw new Exception("证书序列号错误");
        }

        final PublicKey publicKey = x509Certificate.getPublicKey();
        return verifySignature(signature, body, nonceStr, timestamp, publicKey);
    }

    public static boolean verifySignature(Map<String,String> headerMap, String body, String platformCertPath) throws Exception {
        String timestamp = headerMap.get("Wechatpay-Timestamp");
        String nonceStr = headerMap.get("Wechatpay-Nonce");
        String signature = headerMap.get("Wechatpay-Signature");
        String serialNo = headerMap.get(JPayConstants.WX_CERT_SERIAL_NO);

        InputStream inputStream = PayUtil.getCertFileInputStream(platformCertPath);
        // 获取平台证书序列号
        final X509Certificate x509Certificate = PayUtil.getX509Certificate(inputStream);
        ;
        final String serialNUmber = x509Certificate.getSerialNumber().toString(16).toUpperCase();
        // 验证证书序列号
        if (!StrUtil.equals(serialNUmber, serialNo)) {
            log.error("V3 签名验证失败（当前用户持有的微信平台证书序列号 与 应答微信支付的平台证书序列号 不一致）");
            throw new Exception("证书序列号错误");
        }

        final PublicKey publicKey = x509Certificate.getPublicKey();
        return verifySignature(signature, body, nonceStr, timestamp, publicKey);
    }

    /**
     * 验证签名
     *
     * @param signature 待验证的签名
     * @param body      应答主体
     * @param nonce     随机串
     * @param timestamp 时间戳
     * @param publicKey {@link PublicKey} 微信支付平台公钥
     * @return 签名结果
     * @throws Exception 异常信息
     */
    public static boolean verifySignature(String signature,
                                          String body,
                                          String nonce,
                                          String timestamp,
                                          PublicKey publicKey) throws Exception {
        // 构造验签名串
        String buildSignMessage = PayUtil.buildSignMessage(timestamp, nonce, body);
        // 使用微信支付平台公钥进行SHA256 with RSA签名验证
        return WxRsaUtil.checkSignByPublicKey(buildSignMessage, signature, publicKey);
    }


    /**
     * 验证签名
     *
     * @param signature    待验证的签名
     * @param body         应答主体
     * @param nonce        随机串
     * @param timestamp    时间戳
     * @param publicKeyStr 从微信支付平台证书导出微信支付平台公钥字符串， 去掉-----BEGIN PUBLIC KEY----- 和-----END PUBLIC KEY-----
     * @return 签名结果
     * @throws Exception 异常信息
     */
    public static boolean verifySignature(String signature,
                                          String body,
                                          String nonce,
                                          String timestamp,
                                          String publicKeyStr) throws Exception {
        // 构造验签名串
        String buildSignMessage = PayUtil.buildSignMessage(timestamp, nonce, body);
        // 使用微信支付平台公钥进行SHA256 with RSA签名验证
        return WxRsaUtil.checkSignByPublicKey(buildSignMessage, signature, publicKeyStr);
    }

    /**
     * v3 支付异步通知明文
     *
     * @param body     异步通知密文
     * @param apiV3key api v3密钥
     * @return 异步通知明文
     * @throws Exception 异常信息
     */
    public static String getAsyncNotifyPlaintext(String apiV3key, String body) throws Exception {
        JSONObject resultObject = JSONUtil.parseObj(body);
        JSONObject resource = resultObject.getJSONObject("resource");
        String cipherText = resource.getStr("ciphertext");  // Base64编码后的密文
        String nonceStr = resource.getStr("nonce"); // 加密使用的随机串初始化向量）
        String associatedData = resource.getStr("associated_data"); // 附加数据包（可能为空）

        // 证书和回调报文解密
        WxAesUtil aesUtil = new WxAesUtil(apiV3key.getBytes(StandardCharsets.UTF_8));
        return aesUtil.decryptToString(
                associatedData.getBytes(StandardCharsets.UTF_8),
                nonceStr.getBytes(StandardCharsets.UTF_8),
                cipherText
        );
    }

    /**
     * 获取证书序列号
     *
     * @param certPath 证书路径
     * @return {@link String} 证书序列号
     */
    public static String getSerialNumber(String certPath) {
        final X509Certificate x509Certificate = PayUtil.getX509Certificate(certPath);
        if (null != x509Certificate) {
            return x509Certificate.getSerialNumber().toString(16).toUpperCase();
        }
        return null;
    }
}
