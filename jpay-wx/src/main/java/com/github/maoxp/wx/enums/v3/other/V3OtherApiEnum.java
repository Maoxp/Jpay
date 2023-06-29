package com.github.maoxp.wx.enums.v3.other;

import com.github.maoxp.wx.enums.IWxApiEnum;

/**
 * WxApiType
 * <p>其然能力</p>
 *
 * @author mxp
 * @date 2023年06月16日 08:37
 * @since JDK 1.8
 */
public enum V3OtherApiEnum implements IWxApiEnum {
    /**
     * 营销专用-图片上传
     */
    MARKETING_UPLOAD_MEDIA("/v3/marketing/favor/media/image-upload", ""),
    /**
     * 通用接口-图片上传
     */
    MERCHANT_UPLOAD_MEDIA("/v3/merchant/media/upload", "其他能力-图片上传"),
    /**
     * 通用接口-视频上传
     */
    MERCHANT_VIDEO_UPLOAD("/v3/merchant/media/video_upload", "其他能力-视频上传"),
    /**
     * 获取平台证书列表
     */
    GET_CERTIFICATES("/v3/certificates", "其他能力-获取微信支付平台证书"),
    ;


    V3OtherApiEnum(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    /**
     * 接口URL
     */
    private final String url;

    private final String desc;

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "WxApiType{" +
                "url='" + url + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
