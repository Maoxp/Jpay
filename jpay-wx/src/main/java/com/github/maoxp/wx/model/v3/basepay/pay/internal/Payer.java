package com.github.maoxp.wx.model.v3.basepay.pay.internal;


import lombok.Data;

/**
 * Payer-V3 统一下单-支付者
 *
 * @author mxp
 * @date 2023/6/15年06月15日 17:23
 * @since JDK 1.8
 */
@Data
public class Payer {
    /**
     * 用户标识
     */
    private String openid;
    /**
     * 用户服务标识
     */
    private String sp_openid;
    /**
     * 用户子标识
     */
    private String sub_openid;
}
