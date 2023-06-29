package com.github.maoxp.wx.model.v3.basepay.pay.internal;

import lombok.Data;

/**
 * Amount
 *
 * @author mxp
 * @date 2023/6/15年06月15日 16:50
 * @since JDK 1.8
 */
@Data
public class Amount {
    public Amount() {
    }

    public Amount(int total, String currency) {
        this.total = total;
        this.currency = currency;
    }

    public Amount(int total) {
        this.total = total;
    }


    /**
     * 总金额，单位为分。
     * <p>必填：是</p>
     */
    private int total;
    /**
     * 货币类型，CNY：人民币，境内商户号仅支持人民币
     * <p>必填：否</p>
     */
    private String currency = "CNY";
}
