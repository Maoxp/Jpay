package com.github.maoxp.wx.model.v3.basepay.bill;

import com.github.maoxp.wx.model.v3.BaseModel;
import lombok.Data;

/**
 * DownloadFundFlowBill
 * <p>下载资金账单</p>
 *
 * @author mxp
 * @date 2023/6/19年06月19日 15:34
 * @since JDK 1.8
 */
@Data
public class DownloadFundFlowBill extends BaseModel {
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
     * 资金账户类型
     *
     * <p>必填：否</p>
     * <p>Get: query</p>
     *
     * <ul>
     *     <p>不填则默认是BASIC,枚举值:
     *     <li>OPERATION：运营账户</li>
     *     <li>BASIC：基本账户</li>
     *     <li>FEES：手续费账户</li>
     * </ul>
     */
    private String account_type = "BASIC";

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
