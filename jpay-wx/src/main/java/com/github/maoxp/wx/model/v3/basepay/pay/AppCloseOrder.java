package com.github.maoxp.wx.model.v3.basepay.pay;

import com.github.maoxp.wx.model.v3.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AppCloseOrder
 * <p>关闭订单Model</p>
 *
 * @author mxp
 * @date 2023/6/19年06月19日 14:10
 * @since JDK 1.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppCloseOrder extends BaseModel {
    /**
     * 直连商户的商户号，由微信支付生成并下发。
     *
     * <p>必填：是</p>
     * <p>POST：body</p>
     */
    private String mchid;

    /**
     * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。
     *
     * <p>必填：是</p>
     * <p>POST：path</p>
     */
    private String out_trade_no;
}
