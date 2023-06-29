package com.github.maoxp.wx.model.v3.basepay.pay.internal;

import lombok.Data;

/**
 * SettleInfo
 * V3 统一下单-结算信息
 *
 * @author mxp
 * @date 2023/6/15年06月15日 16:56
 * @since JDK 1.8
 */
@Data
public class SettleInfo {
    /**
     * 是否指定分账
     */
    private boolean profit_sharing;
    /**
     * 补差金额
     */
    private Integer subsidy_amount;
}
