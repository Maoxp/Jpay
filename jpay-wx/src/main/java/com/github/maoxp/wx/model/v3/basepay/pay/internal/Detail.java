package com.github.maoxp.wx.model.v3.basepay.pay.internal;

import lombok.Data;

import java.util.List;

/**
 * Detail
 * <p>V3 统一下单-优惠功能</p>
 *
 * @author mxp
 * @date 2023/6/15年06月15日 17:23
 * @since JDK 1.8
 */
@Data
public class Detail {
    /**
     * 订单原价
     * <p>必填: 否</code>
     * <ul>
     *     <li>1、商户侧一张小票订单可能被分多次支付，订单原价用于记录整张小票的交易金额。</li>
     *     <li>2、当订单原价与支付金额不相等，则不享受优惠。</li>
     *     <li>3、该字段主要用于防止同一张小票分多次支付，以享受多次优惠的情况，正常支付订单不必上传此参数。</li>
     * </ul>
     * <p>示例值：608800</p>
     */
    private int cost_price;
    /**
     * 商品小票ID
     * <p>必填: 否</code>
     * <p>示例值：微信123</p>
     */
    private String invoice_id;
    /**
     * 单品列表
     * <p>必填: 否</code>
     * <p>条目个数限制：【1，6000】</p>
     */
    private List<GoodsDetail> goods_detail;

    /**
     * 单品列表
     */
    @Data
    public static class GoodsDetail {
        /**
         * 商户侧商品编码
         * <p>必填: 是</code>
         * <p>由半角的大小写字母、数字、中划线、下划线中的一种或几种组成。</p>
         * <p>示例值：1246464644</p>
         */
        private String merchant_goods_id;
        /**
         * 微信侧商品编码
         * <p>必填: 否</code>
         * <p>微信支付定义的统一商品编号（没有可不传）</p>
         * <p>示例值：1001</p>
         */
        private String wechatpay_goods_id;
        /**
         * 商品名称
         * <p>必填: 否</code>
         * <p>示例值：iPhoneX 256G</p>
         */
        private String goods_name;
        /**
         * 商品单价。
         * <p>必填: 是</code>
         * <p>单位为：分。如果商户有优惠，需传输商户优惠后的单价(例如：用户对一笔100元的订单使用了商场发的纸质优惠券100-50，则活动商品的单价应为原单价-50)</p>
         * <p>示例值：528800</p>
         */
        private int unit_price;
        /**
         * 商品数量
         * <p>必填: 是</code>
         * <p>用户购买的数量</p>
         * <p>示例值：1</p>
         */
        private int quantity;
    }
}
