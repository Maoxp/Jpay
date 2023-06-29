package com.github.maoxp.wx.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.maoxp.core.JPayHttpResponse;
import com.github.maoxp.core.utils.DateTimeZoneUtil;
import com.github.maoxp.wx.WxApi;
import com.github.maoxp.wx.constants.JPayConstants;
import com.github.maoxp.wx.enums.RequestMethodEnum;
import com.github.maoxp.wx.enums.WxDomainEnum;
import com.github.maoxp.wx.enums.v3.other.V3OtherApiEnum;
import com.github.maoxp.wx.model.v3.CertificateModel;
import com.github.maoxp.wx.model.v3.certificate.CertificateInfoRQ;
import com.github.maoxp.wx.model.v3.certificate.CertificateRQ;
import com.github.maoxp.wx.model.v3.certificate.EncryptCertificateRQ;
import com.github.maoxp.wx.property.WxPayApiProperty;
import com.github.maoxp.wx.property.WxPayApiPropertyKit;
import com.github.maoxp.wx.utils.PayUtil;
import com.github.maoxp.wx.utils.WxAesUtil;
import com.github.maoxp.wx.utils.WxPayUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * WxPayCertificateService
 * <p>证书服务</p>
 *
 * @author mxp
 * @date 2023/6/27年06月27日 09:46
 * @since JDK 1.8
 */
@Slf4j
public class WxPayCertificateService {

    /**
     * 获取平台证书列表，用来定期自动更新证书
     *
     * @param platformSerialNo 指定保存的平台证书序列号
     * @return 获取证书结果
     * @throws Exception 异常信息
     */
    public static boolean autoUpdateOrGetCertificate(String platformSerialNo) throws Exception {
        WxPayApiProperty config = WxPayApiPropertyKit.getWxPayApiProperty();
        final JPayHttpResponse response = WxApi.domain(WxDomainEnum.CHINA)
                .v3(
                        RequestMethodEnum.GET,
                        V3OtherApiEnum.GET_CERTIFICATES.getUrl(),
                        config.getMchId(),
                        null,
                        null,
                        config.getKeyPath(),
                        ""
                );
        if (Objects.isNull(response)) {
            return false;
        }
        final String responseSerialNo = response.getHeader(JPayConstants.WX_CERT_SERIAL_NO);
        String body = response.getBody();
        int status = response.getStatus();

        if (status == JPayConstants.CODE_200) {
            final CertificateRQ certificate = JSONUtil.toBean(body, CertificateRQ.class);
            if (null == certificate) {
                log.debug("获取平台证书列表接口-解析返回数据失败");
                return false;
            }
            final List<CertificateInfoRQ> certificateInfoRQS = certificate.getData();
            log.debug("总共获取到 {} 个微信支付平台证书", certificateInfoRQS.size());
            if (CollUtil.isNotEmpty(certificateInfoRQS)) {
                log.debug("获取平台证书列表接口-未获取到任何有效平台证书");
                return false;
            }

            CertificateInfoRQ certificateInfo = null;
            // 获取指定序列号的平台证书
            if (StrUtil.isNotEmpty(platformSerialNo)) {
                final Optional<CertificateInfoRQ> optional = certificateInfoRQS.stream()
                        .filter(item -> platformSerialNo.equals(item.getSerial_no()))
                        .findFirst();
                if (optional.isPresent()) {
                    certificateInfo = optional.get();
                }
            }

            // 指定序列号的平台证书不存在，遍历获取可用的平台证书
            if (null == certificateInfo) {
                log.debug("指定序列号 {} 的平台证书不存在，开始遍历获取可用的平台证书", platformSerialNo);
                final HashMap<String, Date> serialNoEffectiveTimeMap = new HashMap<>(certificateInfoRQS.size());
                for (CertificateInfoRQ info : certificateInfoRQS) {
                    if (null == info) {
                        continue;
                    }
                    final String serialNo = info.getSerial_no();
                    String effectiveTimeStr = DateTimeZoneUtil.timeZoneDateToStr(info.getEffective_time());
                    serialNoEffectiveTimeMap.put(serialNo, DateUtil.parse(effectiveTimeStr).toJdkDate());

                    String expireTimeStr = DateTimeZoneUtil.timeZoneDateToStr(info.getExpire_time());
                    Date expireDate = DateUtil.parse(expireTimeStr);
                    if (expireDate.before(new Date())) {
                        log.debug("序列号 {} 对应的平台证书已过期", serialNo);
                        continue;
                    }
                    log.debug("序列号 {} 对应的平台证书可用,忽略其他证书", serialNo);
                    certificateInfo = info;
                    break;
                }

                // 使用 Collections.max 方法找到启用时间较晚的证书
                Map.Entry<String, Date> maxEntry = Collections.max(serialNoEffectiveTimeMap.entrySet(), Map.Entry.comparingByValue());
                if (Objects.isNull(maxEntry)) {
                    return false;
                }
                final Optional<CertificateInfoRQ> infoRQOptional = certificateInfoRQS.stream()
                        .filter(item -> maxEntry.getKey().equals(item.getSerial_no()))
                        .findFirst();
                if (infoRQOptional.isPresent()) {
                    certificateInfo = infoRQOptional.get();
                    log.debug("序列号 {} 对应的平台证书刚刚启用", maxEntry.getKey());
                }
            }

            if (null == certificateInfo) {
                log.debug("未获取到微信支付平台证书");
                return false;
            }

            // 保存证书的序列号
            String serialNo = certificateInfo.getSerial_no();
            EncryptCertificateRQ encryptCertificate = certificateInfo.getEncrypt_certificate();
            String associatedData = encryptCertificate.getAssociated_data();
            String cipherText = encryptCertificate.getCiphertext();
            String nonce = encryptCertificate.getNonce();
            boolean isOk = savePlatformCert(associatedData, nonce, cipherText, config.getPlatformCertPath());
            if (isOk) {
                log.debug("平台证书保存成功,序列号为 {} 保存路径为 {}", serialNo, config.getPlatformCertPath());
                if (StrUtil.equals(responseSerialNo, serialNo)) {
                    // 根据证书序列号查询对应的证书来验证签名结果
                    boolean verifySignature = WxPayUtil.verifySignature(response, config.getPlatformCertPath());
                    log.debug("使用序列号 {} 对应的平台证书签名验证结果 {}", serialNo, verifySignature);
                    return verifySignature;
                }
                return true;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * 保存证书
     *
     * @param associatedData 关联数据
     * @param nonce          随机数
     * @param cipherText     加密报文
     * @param certPath       证书路径
     * @return 保存结果
     * @throws Exception 异常信息
     */
    protected static boolean savePlatformCert(String associatedData, String nonce, String cipherText, String certPath) throws Exception {
        WxPayApiProperty config = WxPayApiPropertyKit.getWxPayApiProperty();

        // 证书和回调报文解密
        WxAesUtil aesUtil = new WxAesUtil(config.getApiKeyV3().getBytes(StandardCharsets.UTF_8));
        String publicKey = aesUtil.decryptToString(
                associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                cipherText
        );

        // 保存证书
        FileWriter writer = new FileWriter(certPath);
        File file = writer.write(publicKey);
        return file.isFile() && file.exists();
    }

    /**
     * 获取商户证书序列号
     *
     * @return 证书序列号
     */
    protected static String getSerialNumber() {
        final WxPayApiProperty wxPayApiProperty = WxPayApiPropertyKit.getWxPayApiProperty();
        CertificateModel certificateInfo = PayUtil.getCertificateInfo(wxPayApiProperty.getCertPath());
        if (null == certificateInfo) {
            return null;
        }
        return certificateInfo.getSerialNumber();
    }

    /**
     * 获取平台证书序列号
     *
     * @return 证书序列号
     */
    protected String getPlatformSerialNumber() {
        final WxPayApiProperty wxPayApiProperty = WxPayApiPropertyKit.getWxPayApiProperty();
        CertificateModel platformCertificateModel = PayUtil.getCertificateInfo(wxPayApiProperty.getPlatformCertPath());
        if (null == platformCertificateModel) {
            return null;
        }
        return platformCertificateModel.getSerialNumber();
    }
}

