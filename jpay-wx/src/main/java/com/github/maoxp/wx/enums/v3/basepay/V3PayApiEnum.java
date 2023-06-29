package com.github.maoxp.wx.enums.v3.basepay;

import com.github.maoxp.wx.enums.IWxApiEnum;

/**
 * V3PayApiEnum
 * <p>基础支付API</p>
 *
 * @author mxp
 * @date 2023年06月15日 16:31
 * @since JDK 1.8
 */
public enum V3PayApiEnum implements IWxApiEnum {
    /**
     * JSAPI下单
     */
    JS_API_PAY("/v3/pay/transactions/jsapi", "JSAPI 下单"),
    /**
     * APP 下单
     */
    APP_PAY("/v3/pay/transactions/app", "APP 下单"),
    /**
     * H5 下单
     */
    H5_PAY("/v3/pay/transactions/h5", "H5 下单"),
    /**
     * Native 下单
     */
    NATIVE_PAY("/v3/pay/transactions/native", "Native 下单"),
    /**
     * 微信支付订单号查询
     */
    ORDER_QUERY_BY_TRANSACTION_ID("/v3/pay/transactions/id/%s", "微信支付订单号查询"),
    /**
     * 商户订单号查询
     */
    ORDER_QUERY_BY_OUT_TRADE_NO("/v3/pay/transactions/out-trade-no/%s", "商户订单号查询"),
    /**
     * 关闭订单
     */
    CLOSE_ORDER_BY_OUT_TRADE_NO("/v3/pay/transactions/out-trade-no/%s/close", "关闭订单"),
    /**
     * 申请退款
     */
    REFUND("/v3/refund/domestic/refunds", "申请退款"),
    /**
     * 查询单笔退款
     */
    REFUND_QUERY_BY_OUT_REFUND_NO("/v3/refund/domestic/refunds/%s", "查询单笔退款"),
    /**
     * 申请资金账单
     */
    FUND_FLOW_BILL("/v3/bill/fundflowbill", "申请（按天）资金账单，包含资金流水账单文件的下载地址"),
    /**
     * 申请交易账单
     */
    TRADE_BILL("/v3/bill/tradebill", "申请（按天）交易账单文件的下载地址"),
    /**
     * 下载账单
     */
    BILL_DOWNLOAD("/v3/billdownload/file", "下载账单，交易/资金账单都可以通过该接口获取到对应的账单");

    /**
     * 接口URL
     */
    private final String url;

    /**
     * 接口描述
     */
    private final String desc;

    V3PayApiEnum(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "V3PayApiEnum{" +
                "url='" + url + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
