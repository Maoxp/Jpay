package com.github.maoxp.wx.model.v3.basepay.pay.internal;

import lombok.Data;

/**
 * StoreInfo
 * <p>V3 统一下单-商户门店信息</p>
 *
 * @author mxp
 * @date 2023/6/15年06月15日 16:58
 * @since JDK 1.8
 */
@Data
public class StoreInfo {
    /**
     * 门店编号
     */
    private String id;
    /**
     * 门店名称
     */
    private String name;
    /**
     * 地区编码
     */
    private String area_code;
    /**
     * 详细地址
     */
    private String address;
}
