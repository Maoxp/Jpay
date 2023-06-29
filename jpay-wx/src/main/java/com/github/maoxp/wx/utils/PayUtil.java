package com.github.maoxp.wx.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.github.maoxp.core.XmlHelper;
import com.github.maoxp.wx.constants.JPayConstants;
import com.github.maoxp.wx.enums.RequestMethodEnum;
import com.github.maoxp.wx.model.v3.CertificateModel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.*;
import java.util.*;

/**
 * PayKit
 *
 * @author mxp
 * @date 2023/6/19年06月19日 16:38
 * @since JDK 1.8
 */
@Slf4j
public class PayUtil {
    /**
     * 对象路径前缀
     */
    public static final String CLASS_PATH_PREFIX = "classpath:";

    /**
     * 生成16进制的 sha256 字符串
     *
     * @param data 数据
     * @param key  密钥
     * @return sha256 字符串
     */
    public static String hmacSha256(String data, String key) {
        return SecureUtil.hmac(HmacAlgorithm.HmacSHA256, key).digestHex(data);
    }

    /**
     * SHA1加密文件，生成16进制SHA1字符串<br>
     *
     * @param dataFile 被加密文件
     * @return SHA1 字符串
     */
    public static String sha1(File dataFile) {
        return SecureUtil.sha1(dataFile);
    }

    /**
     * SHA1加密，生成16进制SHA1字符串<br>
     *
     * @param data 数据
     * @return SHA1 字符串
     */
    public static String sha1(InputStream data) {
        return SecureUtil.sha1(data);
    }

    /**
     * SHA1加密，生成16进制SHA1字符串<br>
     *
     * @param data 数据
     * @return SHA1 字符串
     */
    public static String sha1(String data) {
        return SecureUtil.sha1(data);
    }

    /**
     * 生成16进制 MD5 字符串
     *
     * @param data 数据
     * @return MD5 字符串
     */
    public static String md5(String data) {
        return SecureUtil.md5(data);
    }

    /**
     * AES 解密
     *
     * @param base64Data 需要解密的数据
     * @param key        密钥
     * @return 解密后的数据
     */
    public static String decryptData(String base64Data, String key) {
        return SecureUtil.aes(md5(key).toLowerCase().getBytes()).decryptStr(base64Data);
    }

    /**
     * AES 加密
     *
     * @param data 需要加密的数据
     * @param key  密钥
     * @return 加密后的数据
     */
    public static String encryptData(String data, String key) {
        return SecureUtil.aes(md5(key).toLowerCase().getBytes()).encryptBase64(data.getBytes());
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的 ThreadLocalRandom 生成UUID
     *
     * @return 简化的 UUID，去掉了横线
     */
    public static String generateStr() {
        return IdUtil.fastSimpleUUID();
    }

    /**
     * 雪花算法
     *
     * @param workerId     终端ID
     * @param dataCenterId 数据中心ID
     * @return {@link Snowflake}
     */
    public static Snowflake getSnowflake(long workerId, long dataCenterId) {
        return IdUtil.getSnowflake(workerId, dataCenterId);
    }

    /**
     * 把所有元素排序
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        return createLinkString(params, false);
    }

    /**
     * @param params 需要排序并参与字符拼接的参数组
     * @param encode 是否进行URLEncoder
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, boolean encode) {
        return createLinkString(params, "&", encode);
    }

    /**
     * @param params  需要排序并参与字符拼接的参数组
     * @param connStr 连接符号
     * @param encode  是否进行URLEncoder
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params, String connStr, boolean encode) {
        return createLinkString(params, connStr, encode, false);
    }

    public static String createLinkString(Map<String, String> params, String connStr, boolean encode, boolean quotes) {
        List<String> keys = new ArrayList<>(params.keySet());
        // 参数名ASCII码从小到大排序（字典序）
        Collections.sort(keys);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            content.append(key).append("=");
            // 拼接时，不包括最后一个&字符
            if (i == keys.size() - 1) {
                if (quotes) {
                    content.append('"').append(encode ? urlEncode(value) : value).append('"');
                } else {
                    content.append(encode ? urlEncode(value) : value);
                }
            } else {
                if (quotes) {
                    content.append('"').append(encode ? urlEncode(value) : value).append('"').append(connStr);
                } else {
                    content.append(encode ? urlEncode(value) : value).append(connStr);
                }
            }
        }
        return content.toString();
    }

    /**
     * URL 编码
     *
     * @param src 需要编码的字符串
     * @return 编码后的字符串
     */
    public static String urlEncode(String src) {
        try {
            return URLEncoder.encode(src, CharsetUtil.UTF_8).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 遍历 Map 并构建 xml 数据
     *
     * @param params 需要遍历的 Map
     * @param prefix xml 前缀
     * @param suffix xml 后缀
     * @return xml 字符串
     */
    public static StringBuffer forEachMap(Map<String, String> params, String prefix, String suffix) {
        StringBuffer xml = new StringBuffer();
        if (CharSequenceUtil.isNotEmpty(prefix)) {
            xml.append(prefix);
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 略过空值
            if (CharSequenceUtil.isEmpty(value)) {
                continue;
            }
            xml.append("<").append(key).append(">");
            xml.append(entry.getValue());
            xml.append("</").append(key).append(">");
        }
        if (CharSequenceUtil.isNotEmpty(suffix)) {
            xml.append(suffix);
        }
        return xml;
    }

    /**
     * 微信下单 map to xml
     *
     * @param params Map 参数
     * @return xml 字符串
     */
    public static String mapToXml(Map<String, String> params) {
        StringBuffer xml = forEachMap(params, "<xml>", "</xml>");
        return xml.toString();
    }

    /**
     * 针对支付的 xml，没有嵌套节点的简单处理
     *
     * @param xmlStr xml 字符串
     * @return 转化后的 Map
     */
    public static Map<String, String> xmlToMap(String xmlStr) {
        XmlHelper xmlHelper = XmlHelper.of(xmlStr);
        return xmlHelper.toMap();
    }


    /**
     * V3构造签名串
     *
     * @param method    {@link RequestMethodEnum} GET,POST,PUT等
     * @param url       请求接口 /v3/certificates
     * @param timestamp 获取发起请求时的系统当前时间戳
     * @param nonceStr  随机字符串
     * @param body      请求报文主体
     * @return 待签名字符串
     */
    public static String buildSignMessage(RequestMethodEnum method, String url, long timestamp, String nonceStr, String body) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(method.getMethod());
        arrayList.add(url);
        arrayList.add(String.valueOf(timestamp));
        arrayList.add(nonceStr);
        arrayList.add(body);
        return buildSignMessage(arrayList);
    }

    /**
     * 构造验签名串
     *
     * @param timestamp 应答时间戳
     * @param nonceStr  应答随机串
     * @param body      应答报文主体
     * @return 应答待签名字符串
     */
    public static String buildSignMessage(String timestamp, String nonceStr, String body) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(timestamp);
        arrayList.add(nonceStr);
        arrayList.add(body);
        return buildSignMessage(arrayList);
    }

    /**
     * 构造签名串
     *
     * @param signMessage 待签名的参数
     * @return 构造后带待签名串
     */
    public static String buildSignMessage(List<String> signMessage) {
        if (CollUtil.isEmpty(signMessage)) {
            return null;
        }

        StringBuilder sbf = new StringBuilder();
        for (String str : signMessage) {
            sbf.append(str).append("\n");
        }
        return sbf.toString();
    }


    /**
     * v3 接口(通过{@link #buildSignMessage}的签名串)创建签名
     *
     * @param signMessage 待签名的参数
     * @param keyPath     key.pem 证书路径
     * @return 生成 v3 签名
     */
    public static String createSignByV3(String signMessage, String keyPath) throws Exception{
        if (CharSequenceUtil.isEmpty(signMessage)) {
            return null;
        }
        // 获取商户私钥
        PrivateKey privateKey = getPrivateKey(keyPath);
        // 生成签名
        return WxRsaUtil.signByPrivateKey(signMessage, privateKey);
    }

    /**
     * V3接口 设置HTTP头传递签名, 由认证类型和签名信息组成。
     *
     * @param mchId     发起请求的商户（包括直连商户、服务商或渠道商）的商户号
     * @param serialNo  商户API证书序列号
     * @param nonceStr  请求随机串
     * @param timestamp 时间戳
     * @param signature 签名值
     * @param authType  认证类型
     * @return 请求头 Authorization
     */
    public static String getAuthorization(String mchId,
                                          String serialNo,
                                          String nonceStr,
                                          String timestamp,
                                          String signature,
                                          String authType) {
        Map<String, String> params = new HashMap<>(5);
        params.put("mchid", mchId);
        params.put("serial_no", serialNo);
        params.put("nonce_str", nonceStr);
        params.put("timestamp", timestamp);
        params.put("signature", signature);
        return authType.concat(" ").concat(createLinkString(params, ",", false, true));
    }

    /**
     * 获取商户私钥文本内容
     *
     * @param keyPath 商户私钥证书路径
     * @return {@link String} 商户私钥文本内容
     * @throws Exception 异常信息
     */
    public static String getPrivateKeyStr(String keyPath) throws Exception {
        final PrivateKey privateKey = getPrivateKey(keyPath);
        return WxRsaUtil.getPrivateKeyStr(privateKey);
    }

    /**
     * 获取商户私钥Key
     *
     * @param keyPath 商户私钥证书路径
     * @return {@link PrivateKey} 商户私钥Key
     * @throws Exception 异常信息
     */
    public static PrivateKey getPrivateKey(String keyPath) throws Exception {
        String originalKeyStr = getCertFileContent(keyPath);
        if (StrUtil.isEmpty(originalKeyStr)) {
            throw new RuntimeException("商户私钥证书获取失败");
        }
        String privateKeyStr = originalKeyStr
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        return WxRsaUtil.loadPrivateKey(privateKeyStr);
    }

    /**
     * 获取证书
     *
     * @param inputStream 证书文件流
     * @return {@link X509Certificate} 获取证书
     */
    public static X509Certificate getX509Certificate(InputStream inputStream) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
            cert.checkValidity();
            return cert;
        } catch (CertificateExpiredException e) {
            throw new RuntimeException("证书已过期", e);
        } catch (CertificateNotYetValidException e) {
            throw new RuntimeException("证书尚未生效", e);
        } catch (CertificateException e) {
            throw new RuntimeException("无效的证书", e);
        }
    }

    /**
     * 获取证书
     *
     * @param path 证书路径，支持相对路径以及绝得路径
     * @return {@link X509Certificate} 获取证书
     */
    public static X509Certificate getX509Certificate(String path) {
        if (StrUtil.isEmpty(path)) {
            return null;
        }
        InputStream inputStream;
        try {
            inputStream = getCertFileInputStream(path);
        } catch (IOException e) {
            throw new RuntimeException("请检查证书路径是否正确", e);
        }
        return getX509Certificate(inputStream);
    }

    /**
     * 获取证书详细信息
     *
     * @param x509Certificate {@link X509Certificate} 证书
     * @return {@link CertificateModel}  获取证书详细信息
     */
    public static CertificateModel getCertificateInfo(X509Certificate x509Certificate) {
        if (null == x509Certificate) {
            return null;
        }
        CertificateModel model = new CertificateModel();
        model.setItself(x509Certificate);
        model.setIssuerDn(x509Certificate.getIssuerDN());
        model.setSubjectDn(x509Certificate.getSubjectDN());
        model.setVersion(x509Certificate.getVersion());
        model.setNotBefore(x509Certificate.getNotBefore());
        model.setNotAfter(x509Certificate.getNotAfter());
        model.setSerialNumber(x509Certificate.getSerialNumber().toString(16));
        return model;
    }

    /**
     * 获取证书详细信息
     *
     * @param path 证书路径，支持相对路径以及绝得路径
     * @return {@link CertificateModel}  获取证书详细信息
     */
    public static CertificateModel getCertificateInfo(String path) {
        X509Certificate certificate = getX509Certificate(path);
        return getCertificateInfo(certificate);
    }

    /**
     * 检查证书是否可用
     *
     * @param model     {@link CertificateModel} 证书详细 model
     * @param mchId     商户号
     * @param offsetDay 偏移天数，正数向未来偏移，负数向历史偏移
     * @return true 有效 false 无效
     */
    public static boolean checkCertificateIsValid(CertificateModel model, String mchId, int offsetDay) {
        if (null == model) {
            return false;
        }
        Date notAfter = model.getNotAfter();
        if (null == notAfter) {
            return false;
        }
        // 证书颁发者
        Principal issuerDn = model.getIssuerDn();
        if (null == issuerDn || !issuerDn.getName().contains(JPayConstants.ISSUER)) {
            return false;
        }
        // 证书CN字段
        if (StrUtil.isNotEmpty(mchId)) {
            Principal subjectDn = model.getSubjectDn();
            if (null == subjectDn || !subjectDn.getName().contains(JPayConstants.CN.concat(mchId.trim()))) {
                return false;
            }
        }
        // 证书序列号固定40字节的字符串
        String serialNumber = model.getSerialNumber();
        if (StrUtil.isEmpty(serialNumber) || serialNumber.length() != JPayConstants.SERIAL_NUMBER_LENGTH) {
            return false;
        }
        // 偏移后的时间
        DateTime dateTime = DateUtil.offsetDay(notAfter, offsetDay);
        DateTime now = DateUtil.date();
        int compare = DateUtil.compare(dateTime, now);
        return compare >= 0;
    }

    /**
     * 检查证书是否可用
     *
     * @param certificate {@link X509Certificate} 证书
     * @param mchId       商户号
     * @param offsetDay   偏移天数，正数向未来偏移，负数向历史偏移
     * @return true 有效 false 无效
     */
    public static boolean checkCertificateIsValid(X509Certificate certificate, String mchId, int offsetDay) {
        if (null == certificate) {
            return false;
        }
        CertificateModel model = getCertificateInfo(certificate);
        return checkCertificateIsValid(model, mchId, offsetDay);
    }

    /**
     * 检查证书是否可用
     *
     * @param path      证书路径，支持相对路径以及绝得路径
     * @param mchId     商户号
     * @param offsetDay 偏移天数，正数向未来偏移，负数向历史偏移
     * @return true 有效 false 无效
     */
    public static boolean checkCertificateIsValid(String path, String mchId, int offsetDay) {
        return checkCertificateIsValid(getCertificateInfo(path), mchId, offsetDay);
    }

    /**
     * 传入 classPath 静态资源路径返回文件输入流
     *
     * @param classPath 静态资源路径
     * @return InputStream
     */
    public static InputStream getFileToStream(String classPath) {
        Resource resource = new ClassPathResource(classPath);
        return resource.getStream();
    }

    /**
     * 传入 classPath 静态资源路径返回绝对路径
     *
     * @param classPath 静态资源路径
     * @return 绝对路径
     */
    public static String getAbsolutePath(String classPath) {
        return new ClassPathResource(classPath).getAbsolutePath();
    }

    /**
     * 通过路径获取证书文件的输入流
     *
     * @param path 文件路径
     * @return 文件流
     * @throws IOException 异常信息
     */
    public static InputStream getCertFileInputStream(String path) throws IOException {
        return FileUtil.getInputStream(path);
//        if (StrUtil.isBlank(path)) {
//            return null;
//        }
//        // 绝对地址
//        File file = new File(path);
//        if (file.exists()) {
//            return Files.newInputStream(file.toPath());
//        }
//        // 相对地址
//        return getFileToStream(path);
    }

    /**
     * 通过路径获取证书文件的内容
     *
     * @param path 文件路径
     * @return 文件内容
     */
    public static String getCertFileContent(String path) throws IOException {
        InputStream certFileInputStream = getCertFileInputStream(path);
        return IoUtil.read(certFileInputStream, StandardCharsets.UTF_8);
    }

    /**
     * 获取文件真实路径
     *
     * @param path 文件地址
     * @return 返回文件真实路径
     */
    public static String getFilePath(String path) {
        if (StrUtil.startWith(path, CLASS_PATH_PREFIX)) {
            return getAbsolutePath(path);
        } else {
            return path;
        }
    }
}
