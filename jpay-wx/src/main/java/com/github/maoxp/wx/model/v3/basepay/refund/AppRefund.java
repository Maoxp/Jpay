package com.github.maoxp.wx.model.v3.basepay.refund;

import com.github.maoxp.wx.model.v3.BaseModel;
import com.github.maoxp.wx.model.v3.basepay.refund.internal.RefundAmount;
import com.github.maoxp.wx.model.v3.basepay.refund.internal.RefundGoodsDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AppRefund
 * <p>V3-申请退款</p>
 *
 * @author mxp
 * @date 2023/6/19年06月19日 14:56
 * @since JDK 1.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppRefund extends BaseModel {
    /**
     * 微信支付订单号
     * <p>必填：二选一</p>
     * <p>POST：body</p>
     */
    private String transaction_id;

    /**
     * 商户订单号
     * <p>必填：二选一</p>
     * <p>POST：body</p>
     */
    private String out_trade_no;

    /**
     * 商户退款单号
     * <p>必填：是</p>
     * <p>POST：body</p>
     * <p>商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。</p>
     */
    private String out_refund_no;

    /**
     * 退款原因
     * <p>必填：否</p>
     * <p>POST：body</p>
     */
    private String reason;

    /**
     * 订单金额信息
     * <p>必填：是</p>
     * <p>POST：body</p>
     */
    private RefundAmount amount;

    /**
     * 指定商品退款需要传此参数，其他场景无需传递
     * <p>必填：否</p>
     * <p>POST：body</p>
     */
    private RefundGoodsDetail goods_detail;

    /**
     * 退款资金来源
     * <p>必填：否</p>
     * <p>POST：body</p>
     * <p>若传递此参数则使用对应的资金账户退款，否则默认使用未结算资金退款（仅对老资金流商户适用）</p>
     */
    private String funds_account;

    /**
     * 退款结果回调url
     * <p>必填：否</p>
     * <p>POST：body</p>
     *
     * <p>如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效，优先回调当前传的这个地址。枚举值：
     * AVAILABLE：可用余额账户</p>
     * <p>示例值：AVAILABLE</p>
     */
    private String notify_url;
}
