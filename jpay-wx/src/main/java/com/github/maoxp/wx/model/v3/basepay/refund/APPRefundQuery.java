package com.github.maoxp.wx.model.v3.basepay.refund;

import com.github.maoxp.wx.model.v3.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * APPRefundQuery
 * <p>V3-查询单笔退款API</p>
 *
 * @author mxp
 * @date 2023/6/19年06月19日 15:29
 * @since JDK 1.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class APPRefundQuery extends BaseModel {
    /**
     * 商户退款单号
     * <p>必填：是</p>
     * <p>GET: path</p>
     */
    private String out_refund_no;
}
