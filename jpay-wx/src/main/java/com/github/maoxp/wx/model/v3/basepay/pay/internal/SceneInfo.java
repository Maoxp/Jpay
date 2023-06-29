package com.github.maoxp.wx.model.v3.basepay.pay.internal;

import lombok.Data;

/**
 * SceneInfo
 * <p>V3 统一下单-场景信息</p>
 *
 * @author mxp
 * @date 2023/6/15年06月15日 16:58
 * @since JDK 1.8
 */
@Data
public class SceneInfo {
    /**
     * 用户终端IP
     * <p>必填：是</p>
     * <p>用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。</p>
     * <p>示例值：14.23.150.211</p>
     */
    private String payer_client_ip;

    /**
     * 商户端设备号
     * <p>必填：否</p>
     * <p>商户端设备号（门店号或收银设备ID）。</p>
     */
    private String device_id;

    /**
     * 商户门店信息
     * <p>必填：否</p>
     */
    private StoreInfo store_info;

    /**
     * H5 场景信息
     * <p>必填：是</p>
     */
    private H5Info h5_info;
}
