package com.github.maoxp.wx.model.v3.basepay.bill;

import com.github.maoxp.wx.model.v3.BaseModel;
import lombok.Data;

/**
 * DownloadTradeBill
 * <p>下载交易账单</p>
 *
 * @author mxp
 * @date 2023/6/19年06月19日 15:34
 * @since JDK 1.8
 */
@Data
public class DownloadTradeBill extends BaseModel {
    /**
     * 账单日期
     *
     * <p>必填：是</p>
     * <p>Get: query</p>
     *
     * <p>格式yyyy-MM-dd
     * 仅支持三个月内的账单下载申请。</p>
     */
    private String bill_date;

    /**
     * 账单类型
     *
     * <p>必填：否</p>
     * <p>Get: query</p>
     *
     * <ul>
     *     <p>不填则默认是ALL,枚举值:
     *     <li>ALL：返回当日所有订单信息（不含充值退款订单）</li>
     *     <li>SUCCESS：返回当日成功支付的订单（不含充值退款订单）</li>
     *     <li>REFUND：返回当日退款订单（不含充值退款订单）</li>
     * </ul>
     */
    private String bill_type = "ALL";

    /**
     * 压缩类型
     *
     * <p>必填：否</p>
     * <p>Get: query</p>
     * <ul>
     *     <p>不填则默认是数据流,枚举值:
     *     <li>GZIP：返回格式为.gzip的压缩包账单</li>
     * </ul>
     */
    private String tar_type;
}
