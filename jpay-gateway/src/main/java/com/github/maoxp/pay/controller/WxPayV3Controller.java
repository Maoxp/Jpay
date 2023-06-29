/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.maoxp.pay.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.maoxp.core.JPayHttpResponse;
import com.github.maoxp.core.utils.HttpUtil;
import com.github.maoxp.wx.WxApi;
import com.github.maoxp.wx.constants.JPayConstants;
import com.github.maoxp.wx.enums.RequestMethodEnum;
import com.github.maoxp.wx.enums.WxDomainEnum;
import com.github.maoxp.wx.enums.v3.basepay.V3PayApiEnum;
import com.github.maoxp.wx.model.v3.basepay.pay.AppUnifiedOrder;
import com.github.maoxp.wx.model.v3.basepay.pay.internal.Amount;
import com.github.maoxp.wx.model.v3.basepay.refund.AppRefund;
import com.github.maoxp.wx.model.v3.basepay.refund.internal.RefundAmount;
import com.github.maoxp.wx.model.v3.basepay.refund.internal.RefundGoodsDetail;
import com.github.maoxp.wx.property.WxPayApiProperty;
import com.github.maoxp.wx.utils.PayUtil;
import com.github.maoxp.wx.utils.WxPayUtil;
import com.github.maoxp.wx.utils.WxRsaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/wxPay/v3")
@RequiredArgsConstructor
@Slf4j
public class WxPayV3Controller {
    private final WxPayApiProperty wxPayApiProperty;


    @GetMapping(value = "/appPay")
    public String appPay() throws Exception {
        final DateTime dateTime = DateUtil.date(System.currentTimeMillis() + 1000 * 60 * 3);
        String timeExpire = dateTime.toString("yyyy-MM-dd'T'HH:mm:ssXXX");

        final AppUnifiedOrder appUnifiedOrder = new AppUnifiedOrder();
        appUnifiedOrder.setAppid(wxPayApiProperty.getAppId());
        appUnifiedOrder.setMchid(wxPayApiProperty.getMchId());
        appUnifiedOrder.setDescription("Image形象店-深圳腾大-QQ公仔");
        appUnifiedOrder.setOut_trade_no(PayUtil.generateStr());
        appUnifiedOrder.setTime_expire(timeExpire);
        appUnifiedOrder.setAttach("附加数据");
        appUnifiedOrder.setNotify_url(wxPayApiProperty.getDomain().concat("/wxPay/v3/payNotify"));
        appUnifiedOrder.setAmount(new Amount(1));
        final String body = JSONUtil.toJsonStr(appUnifiedOrder);
        log.info("APP下单参数 {}", body);

        final JPayHttpResponse jPayHttpResponse = WxApi.domain(WxDomainEnum.CHINA).v3(
                RequestMethodEnum.POST,
                V3PayApiEnum.APP_PAY.getUrl(),
                wxPayApiProperty.getMchId(),
                WxPayUtil.getSerialNumber(wxPayApiProperty.getCertPath()),
                "",
                wxPayApiProperty.getKeyPath(),
                body
        );
        log.info("统一下单响应 {}", jPayHttpResponse);
        if (null == jPayHttpResponse || jPayHttpResponse.getStatus() != JPayConstants.CODE_200) {
            return "app下单，请求失败";
        }

        // 根据证书序列号查询对应的证书来验证签名结果
        final boolean verifySignature = WxPayUtil.verifySignature(jPayHttpResponse, wxPayApiProperty.getPlatformCertPath());
        if (!verifySignature) {
            return "验证签名错误";
        }
        final String responseBody = jPayHttpResponse.getBody();
        final JSONObject jsonObject = JSONUtil.parseObj(responseBody);
        final String prepayId = jsonObject.getStr("prepay_id");
        final Map<String, String> appCreateSign = WxPayUtil.appCreateSign(wxPayApiProperty.getAppId(), wxPayApiProperty.getMchId(), prepayId, wxPayApiProperty.getKeyPath());
        log.info("唤起支付参数:{}", appCreateSign);
        return JSONUtil.toJsonStr(appCreateSign);
    }

    @GetMapping(value = "/queryOrderByOutTradeNo")
    public String queryOrderByOutTradeNo(@RequestParam String outTradeNo) throws Exception {
        Map<String, String> params = new HashMap<>(16);
        params.put("mchid", wxPayApiProperty.getMchId());

        final JPayHttpResponse jPayHttpResponse = WxApi.domain(WxDomainEnum.CHINA).v3(
                RequestMethodEnum.GET,
                String.format(V3PayApiEnum.ORDER_QUERY_BY_OUT_TRADE_NO.getUrl(), outTradeNo),
                wxPayApiProperty.getMchId(),
                WxPayUtil.getSerialNumber(wxPayApiProperty.getCertPath()),
                "",
                wxPayApiProperty.getKeyPath(),
                params
        );
        log.info("商户订单号查询响应 {}", jPayHttpResponse);
        if (null == jPayHttpResponse || jPayHttpResponse.getStatus() != JPayConstants.CODE_200) {
            return "商户订单号查询，请求失败";
        }

        // 根据证书序列号查询对应的证书来验证签名结果
        final boolean verifySignature = WxPayUtil.verifySignature(jPayHttpResponse, wxPayApiProperty.getPlatformCertPath());
        if (!verifySignature) {
            return "验证签名错误";
        }

        return JSONUtil.toJsonStr(jPayHttpResponse);
    }

    @GetMapping("/cipher")
    public String cipher() {
        try {
            // 敏感信息加密, 声明加密使用的平台公钥
            X509Certificate x509Certificate = PayUtil.getX509Certificate(FileUtil.getInputStream(wxPayApiProperty.getPlatformCertPath()));
            String encrypt = WxRsaUtil.rsaEncryptOAEP("敏感信息", x509Certificate);
            log.info("加密结果，密文:{}", encrypt);

            // 敏感信息解密, 申明解密使用的商户私钥
            String encryptStr = "";
            PrivateKey privateKey = PayUtil.getPrivateKey(wxPayApiProperty.getKeyPath());
            String decrypt = WxRsaUtil.rsaDecryptOAEP(encryptStr, privateKey);
            log.info("解密结果,  明文:{}", decrypt);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return null;
    }


    /**
     * 申请交易账单
     *
     * @param billDate 2020-06-14 当天账单后一天出，不然会出现「账单日期格式不正确」错误
     * @return 交易账单下载地址
     */
    @GetMapping("/tradeBill")
    public String tradeBill(@RequestParam(value = "billDate", required = false) String billDate) {
        try {
            billDate =LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            Map<String, String> params = new HashMap<>(12);
            params.put("bill_date", billDate);
            params.put("bill_type", "ALL");
            params.put("tar_type", "GZIP");

            final JPayHttpResponse jPayHttpResponse = WxApi.domain(WxDomainEnum.CHINA).v3(
                    RequestMethodEnum.GET,
                    V3PayApiEnum.TRADE_BILL.getUrl(),
                    wxPayApiProperty.getMchId(),
                    WxPayUtil.getSerialNumber(wxPayApiProperty.getCertPath()),
                    "",
                    wxPayApiProperty.getKeyPath(),
                    params
            );
            log.info("申请交易账单, 查询响应 {}", jPayHttpResponse);
            if (null == jPayHttpResponse || jPayHttpResponse.getStatus() != JPayConstants.CODE_200) {
                return "申请交易账单，请求失败";
            }

            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayUtil.verifySignature(jPayHttpResponse, wxPayApiProperty.getPlatformCertPath());
            if (!verifySignature) {
                return "验证签名错误";
            }
            return JSONUtil.toJsonStr(jPayHttpResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @GetMapping("/billDownload")
    public String billDownload(@RequestParam(value = "token") String token,
                               @RequestParam(value = "tarType", required = false) String tarType) {
        try {

            Map<String, String> params = new HashMap<>(12);
            params.put("token", token);
            if (StrUtil.isNotEmpty(tarType)) {
                params.put("tartype", tarType);
            }
            final JPayHttpResponse jPayHttpResponse = WxApi.domain(WxDomainEnum.CHINA).v3(
                    RequestMethodEnum.GET,
                    V3PayApiEnum.BILL_DOWNLOAD.getUrl(),
                    wxPayApiProperty.getMchId(),
                    WxPayUtil.getSerialNumber(wxPayApiProperty.getCertPath()),
                    "",
                    wxPayApiProperty.getKeyPath(),
                    params
            );
            log.info("下载账单, 查询响应 {}", jPayHttpResponse);

            return JSONUtil.toJsonStr(jPayHttpResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    /**
     * 申请资金账单
     *
     * @param billDate 2020-06-14 当天账单后一天出，不然会出现「账单日期格式不正确」错误
     * @return 资金账单下载地址
     */
    @RequestMapping(value = "/fundFlowBill", method = RequestMethod.GET)
    public String fundFlowBill(@RequestParam(value = "billDate", required = false) String billDate) {
        try {
            billDate =LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Map<String, String> params = new HashMap<>(12);
            params.put("bill_date", billDate);
            params.put("account_type", "BASIC");


            final JPayHttpResponse jPayHttpResponse = WxApi.domain(WxDomainEnum.CHINA).v3(
                    RequestMethodEnum.GET,
                    V3PayApiEnum.FUND_FLOW_BILL.getUrl(),
                    wxPayApiProperty.getMchId(),
                    WxPayUtil.getSerialNumber(wxPayApiProperty.getCertPath()),
                    "",
                    wxPayApiProperty.getKeyPath(),
                    params
            );
            log.info("申请资金账单, 查询响应 {}", jPayHttpResponse);
            if (null == jPayHttpResponse || jPayHttpResponse.getStatus() != JPayConstants.CODE_200) {
                return "申请资金账单，请求失败";
            }

            // 根据证书序列号查询对应的证书来验证签名结果
            boolean verifySignature = WxPayUtil.verifySignature(jPayHttpResponse, wxPayApiProperty.getPlatformCertPath());
            if (!verifySignature) {
                return "验证签名错误";
            }
            return JSONUtil.toJsonStr(jPayHttpResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping(value = "/refund", method = RequestMethod.GET)
    public String refund(@RequestParam(required = false) String transactionId,
                         @RequestParam(required = false) String outTradeNo) throws Exception {
        String outRefundNo = PayUtil.generateStr();
        log.info("商户退款单号: {}", outRefundNo);

        final RefundAmount refundAmount = new RefundAmount();
        refundAmount.setRefund(9);
        refundAmount.setTotal(10);

        final RefundGoodsDetail refundGoodsDetail = new RefundGoodsDetail();
        refundGoodsDetail.setGoods_name("测试商品");
        refundGoodsDetail.setUnit_price(1);
        refundGoodsDetail.setRefund_quantity(9);
        refundGoodsDetail.setRefund_amount(9);
        refundGoodsDetail.setMerchant_goods_id("1217752501201407033233368018");

        final AppRefund appRefund = new AppRefund();
        if (StrUtil.isNotEmpty(transactionId)) {
            appRefund.setTransaction_id(transactionId);
        }
        if (StrUtil.isNotEmpty(outTradeNo)) {
            appRefund.setOut_trade_no(outTradeNo);
        }
        appRefund.setOut_refund_no(outRefundNo);
        appRefund.setReason("东西差退款");
        appRefund.setNotify_url(wxPayApiProperty.getDomain().concat("/wxPay/v3/payNotify"));
        appRefund.setAmount(refundAmount);
        appRefund.setGoods_detail(refundGoodsDetail);

        log.info("退款参数 {}", JSONUtil.toJsonStr(appRefund));


        final JPayHttpResponse jPayHttpResponse = WxApi.domain(WxDomainEnum.CHINA).v3(
                RequestMethodEnum.GET,
                V3PayApiEnum.REFUND.getUrl(),
                wxPayApiProperty.getMchId(),
                WxPayUtil.getSerialNumber(wxPayApiProperty.getCertPath()),
                "",
                wxPayApiProperty.getKeyPath(),
                JSONUtil.toJsonStr(appRefund)
        );
        log.info("申请退款, 查询响应 {}", jPayHttpResponse);
        if (null == jPayHttpResponse || jPayHttpResponse.getStatus() != JPayConstants.CODE_200) {
            return "申请退款，请求失败";
        }

        // 根据证书序列号查询对应的证书来验证签名结果
        boolean verifySignature = WxPayUtil.verifySignature(jPayHttpResponse, wxPayApiProperty.getPlatformCertPath());
        if (!verifySignature) {
            return "验证签名错误";
        }
        return JSONUtil.toJsonStr(jPayHttpResponse);
    }

    @RequestMapping(value = "/payNotify", method = {RequestMethod.POST, RequestMethod.GET})
    public void payNotify(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = new HashMap<>(12);
        try {
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serialNo = request.getHeader("Wechatpay-Serial");
            String signature = request.getHeader("Wechatpay-Signature");

            log.info("timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
            String body = HttpUtil.readData(request);
            log.info("支付通知密文 {}", body);

            Map<String, String> headersMap = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                String headerValue = request.getHeader(headerName);
                headersMap.put(headerName, headerValue);
            }


            // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
            final boolean verifySignature = WxPayUtil.verifySignature(headersMap, body, wxPayApiProperty.getPlatformCertPath());
            if (verifySignature) {
                String plainText =WxPayUtil.getAsyncNotifyPlaintext(wxPayApiProperty.getApiKeyV3(), body);
                log.info("支付通知明文 {}", plainText);

                if (StrUtil.isNotEmpty(plainText)) {
                    response.setStatus(200);
                    map.put("code", "SUCCESS");
                    map.put("message", "SUCCESS");
                } else {
                    response.setStatus(500);
                    map.put("code", "ERROR");
                    map.put("message", "解析支付通知报文错误");
                }
            } else {
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "签名错误");
            }

            response.setHeader("Content-type", ContentType.JSON.toString());
            response.getOutputStream().write(JSONUtil.toJsonStr(map).getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
