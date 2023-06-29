package com.github.maoxp.wx.model.v3.basepay.refund.internal;

import lombok.Data;

/**
 * Amount
 * <p>V3-申请退款-金额信息</p>
 *
 * @author mxp
 * @date 2023/6/19年06月19日 15:01
 * @since JDK 1.8
 */
@Data
public class RefundAmount {
    /**
     * 退款金额
     * <p>必填：是</p>
     * <p>退款金额，单位为分，只能为整数，不能超过原订单支付金额。</p>
     */
    private int refund;

    /**
     * 原订单金额
     * <p>必填：是</p>
     * <p>原支付交易的订单总金额，单位为分，只能为整数。</p>
     */
    private int total;
    /**
     * 货币类型，符合ISO 4217标准的三位字母代码，目前只支持人民币：CNY。
     * <p>必填：是</p>
     */
    private String currency = "CNY";
}
