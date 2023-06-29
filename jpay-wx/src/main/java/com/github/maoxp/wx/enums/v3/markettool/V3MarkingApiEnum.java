package com.github.maoxp.wx.enums.v3.markettool;

import com.github.maoxp.wx.enums.IWxApiEnum;

/**
 * V3MarkingApiEnum
 * <p>营销工具</p>
 *
 * @author mxp
 * @date 2023年06月27日 08:55
 * @since JDK 1.8
 */
public enum V3MarkingApiEnum implements IWxApiEnum {
    /**
     * 营销专用-图片上传
     */
    MARKETING_UPLOAD_MEDIA("/v3/marketing/favor/media/image-upload", "营销专用-图片上传"),
    ;

    V3MarkingApiEnum(String url, String desc) {
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
}
