package com.github.maoxp.core.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * RandomUtil
 *
 * @author mxp
 * @since JDK 1.8
 */
@UtilityClass
public class RandomUtil {
    /**
     * 随机字符串
     */
    private static final String INT_TEMP = "0123456789";
    private static final String STR_TEMP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALL_TEMP = INT_TEMP + STR_TEMP;

    private final Random randomInstant = new Random();

    /**
     * bound以内随机整数
     *
     * @param bound 边界
     * @return int
     */
    public int rdmValue(int bound) {
        return randomInstant.nextInt(bound);
    }

    /**
     * 从List中随机一个value
     *
     * @param values List
     * @param <T>    范型
     * @return T
     */
    public <T> T rdmValue(List<T> values) {
        int index = rdmValue(values.size());
        return values.get(index);
    }

    /**
     * 从Array中随机一个value
     *
     * @param values array
     * @param <T>    范型
     * @return T
     */
    public <T> T rdmValue(T[] values) {
        int index = rdmValue(values.length);
        return values[index];
    }

    /**
     * 随机整数字符串
     *
     * @param length 长度
     * @return {String}
     */
    public String intStr(int length) {
        char[] buffer = new char[length];
        for (int i = 0; i < length; i++) {
            buffer[i] = INT_TEMP.charAt(rdmValue(INT_TEMP.length()));
        }
        return new String(buffer);
    }

    /**
     * 随机大小写英文字母字符串
     *
     * @param length 长度
     * @return {String}
     */
    public String alphaStr(int length) {
        char[] buffer = new char[length];
        for (int i = 0; i < length; i++) {
            buffer[i] = STR_TEMP.charAt(rdmValue(STR_TEMP.length()));
        }
        return new String(buffer);
    }

    /**
     * 随机（字母和数字组合）字符串
     *
     * @param length 最大长度
     * @return {String}
     */
    public String alphaAndIntStr(int length) {
        char[] buffer = new char[length];
        for (int i = 0; i < length; i++) {
            buffer[i] = ALL_TEMP.charAt(rdmValue(ALL_TEMP.length()));
        }
        return new String(buffer);
    }

    /**
     * 随机密码（字母大小写、阿拉伯数字、特殊字）
     *
     * @return {String}
     */
    public String passwdStr(int length) {
        String str = "ABCDE#$@!FGHIJKLMN#$@!OPQRSTUVWXYZabc#$@!defghijklmnop#$@!qrstuvwxyz0123456789#$@!-^&";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = rdmValue(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 获取UUID，去掉`-`的
     *
     * @return {String}
     */
    public String uuidStr() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 唯一订单号
     *
     * @return {String}
     */
    public String getOutTradeNo() {
        String localMillisecond = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmssSSS"));
        return localMillisecond + System.currentTimeMillis();
    }

    /**
     * 订单号必须唯一，可控制其长度
     *
     * @return {String}
     */
    public String getOutTradeNo(int length) {
        final String outTradeNo = getOutTradeNo();
        return outTradeNo.substring(0, length);
    }
}
