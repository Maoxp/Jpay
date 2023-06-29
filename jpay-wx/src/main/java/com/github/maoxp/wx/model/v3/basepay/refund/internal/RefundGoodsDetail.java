package com.github.maoxp.wx.model.v3.basepay.refund.internal;

import lombok.Data;

/**
 * GoodsDetail
 * <p>V3-申请退款-退款商品</p>
 *
 * @author mxp
 * @date 2023/6/19年06月19日 15:08
 * @since JDK 1.8
 */
@Data
public class RefundGoodsDetail {
    /**
     * 商户侧商品编码。
     * <p>必填: 是</p>
     * <p>由半角的大小写字母、数字、中划线、下划线中的一种或几种组成</p>
     * <p>示例值：1217752501201407033233368018</p>
     */
    private String merchant_goods_id;

    /**
     * 微信侧商品编码。
     * <p>必填: 否</p>
     * <p>微信支付定义的统一商品编号（没有可不传）</p>
     * <p>示例值：1001</p>
     */
    private String wechatpay_goods_id;

    /**
     * 商品的实际名称
     * <p>必填: 否</p>
     * <p>示例值：iPhone6s 16G</p>
     */
    private String goods_name;

    /**
     * 商品单价金额，单位为分
     * <p>必填: 是</p>
     */
    private int unit_price;

    /**
     * 商品退款金额，单位为分
     * <p>必填: 是</p>
     */
    private int refund_amount;

    /**
     * 单品的退款数量
     * <p>必填: 是</p>
     */
    private int refund_quantity;
}
