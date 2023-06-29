package com.github.maoxp.wx.enums;

/**
 * SignType
 * <p>签名方式</p>
 *
 * @author maoxp
 */
public enum SignTypeEnum {
    /**
     * HMAC-SHA256 加密
     */
    HMAC_SHA256("HMAC-SHA256"),
    /**
     * MD5 加密
     */
    MD5("MD5"),
    /**
     * RSA
     */
    RSA("RSA");

    SignTypeEnum(String type) {
        this.type = type;
    }

    private final String type;

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "SignTypeEnum{" +
                "type='" + type + '\'' +
                '}';
    }
}
