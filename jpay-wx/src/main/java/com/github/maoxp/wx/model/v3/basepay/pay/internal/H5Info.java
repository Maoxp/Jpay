package com.github.maoxp.wx.model.v3.basepay.pay.internal;

import lombok.Data;

/**
 * <p>V3 统一下单-H5 场景信息</p>
 *
 * @author mxp
 * @date 2023/6/15年06月15日 16:50
 * @since JDK 1.8
 */

@Data
public class H5Info {
    /**
     * 场景类型
     */
    private String type;
    /**
     * 应用名称
     */
    private String app_name;
    /**
     * 网站URL
     */
    private String app_url;
    /**
     * iOS 平台 BundleID
     */
    private String bundle_id;
    /**
     * Android 平台 PackageName
     */
    private String package_name;
}
