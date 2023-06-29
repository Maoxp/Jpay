package com.github.maoxp.wx.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * IWxApi
 * <p>微信API枚举接口</p>
 *
 * @author mxp
 * @date 2023年06月15日 16:26
 * @since JDK 1.8
 */
public interface IWxApiEnum {
    /**
     * 获取枚举URL
     *
     * @return 枚举编码
     */
    String getUrl();

    /**
     * 获取详细的描述信息
     *
     * @return 描述信息
     */
    String getDesc();

    /**
     * 根据 url 获取枚举值
     *
     * @param enumClass 枚举class
     * @param url       url
     * @param <E>       枚举类
     * @return 枚举值
     */
    static <E extends Enum<?> & IWxApiEnum> Optional<E> urlOf(Class<E> enumClass, String url) {
        return Arrays.stream(enumClass.getEnumConstants()).filter(e -> e.getUrl().equals(url)).findFirst();
    }
}
