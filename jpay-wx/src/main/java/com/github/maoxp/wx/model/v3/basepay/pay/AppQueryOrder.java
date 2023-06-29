package com.github.maoxp.wx.model.v3.basepay.pay;

import com.github.maoxp.wx.model.v3.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AppQueryOrder
 * <p>查询订单API</p>
 * <p>商户可以通过查询订单接口主动查询订单状态，完成下一步的业务逻辑。查询订单状态可通过微信支付订单号或商户订单号两种方式查询</p>
 *
 * @author mxp
 * @date 2023/6/15年06月15日 17:28
 * @since JDK 1.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppQueryOrder extends BaseModel {
    /**
     * 直连商户号
     *
     * <p>必填：是</p>
     * <p>GET：query</p>
     */
    private String mchid;

    /**
     * 1.微信支付订单号查询
     *
     * <p>必填：是</p>
     * <p>GET：path</p>
     * <p>微信支付系统生成的订单号</p>
     */
    private String transaction_id;

    /**
     * 2.商户订单号查询
     *
     * <p>必填：是</p>
     * <p>GET：path</p>
     * <p>商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。</p>
     */
    private String out_trade_no;
}
