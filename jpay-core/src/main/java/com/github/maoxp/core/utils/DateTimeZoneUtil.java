package com.github.maoxp.core.utils;

import cn.hutool.core.util.StrUtil;
import com.xkzhangsan.time.converter.DateTimeConverterUtil;
import com.xkzhangsan.time.formatter.DateTimeFormatterUtil;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * DateTimeZoneUtil
 * 时间工具类
 * <p>
 * 依赖 xk-time
 *
 * @author mxp
 * @date 2023/6/27年06月27日 15:12
 * @since JDK 1.8
 */
public class DateTimeZoneUtil implements Serializable {

    private static final long serialVersionUID = -1331008203306650395L;

    /**
     * 时间转 TimeZone
     * <p>
     * 2020-08-17T16:46:37+08:00
     *
     * @param time 时间戳
     * @return {@link String}  TimeZone 格式时间字符串
     * @throws Exception 异常信息
     */
    public static String dateToTimeZone(long time) throws Exception {
        return dateToTimeZone(new Date(time));
    }

    /**
     * 时间转 TimeZone
     * <p>
     * 2020-08-17T16:46:37+08:00
     *
     * @param date {@link Date}
     * @return {@link String} TimeZone 格式时间字符串
     * @throws Exception 异常信息
     */
    public static String dateToTimeZone(Date date) throws Exception {
        String time;
        if (date == null) {
            throw new Exception("date is not null");
        }
        ZonedDateTime zonedDateTime = DateTimeConverterUtil.toZonedDateTime(date);
        time = DateTimeFormatterUtil.format(zonedDateTime, DateTimeFormatterUtil.YYYY_MM_DD_T_HH_MM_SS_XXX_FMT);
        return time;
    }

    /**
     * TimeZone 时间转标准时间
     * <p>
     * 2020-08-17T16:46:37+08:00 to 2020-08-17 16:46:37
     *
     * @param str TimeZone格式时间字符串
     * @return {@link String} 标准时间字符串
     * @throws Exception 异常信息
     */
    public static String timeZoneDateToStr(String str) throws Exception {
        String time;
        if (StrUtil.isBlank(str)) {
            throw new Exception("str is not null");
        }
        ZonedDateTime zonedDateTime = DateTimeFormatterUtil.parseToZonedDateTime(str, DateTimeFormatterUtil.YYYY_MM_DD_T_HH_MM_SS_XXX_FMT);
        if (zonedDateTime == null) {
            throw new Exception("str to zonedDateTime fail");
        }
        time = zonedDateTime.format(DateTimeFormatterUtil.YYYY_MM_DD_HH_MM_SS_FMT);
        return time;
    }

//
//    public static void main(String[] args) throws Exception {
//        String timeZone = dateToTimeZone(System.currentTimeMillis() + 1000 * 60 * 3);
//        System.out.println("timeZone: " + timeZone);
//        String timeZone2 = dateToTimeZone(new Date());
//        System.out.println("timeZone2: " + timeZone2);
//        String date = timeZoneDateToStr(timeZone);
//        System.out.println("date: " + date);
//        System.out.println(DateUtil.parse(date).toDateStr());
//
//
//        Map<String, Date> map = new HashMap<>();
//        map.put("date1", new Date());
//        map.put("date2", new Date(System.currentTimeMillis() - 1000000));
//        map.put("date3", new Date(System.currentTimeMillis() + 1000000));
//
//        // 使用 Comparator.comparing 方法创建一个比较器，比较日期的大小
//        Comparator<Map.Entry<String, Date>> comparator = Comparator.comparing(Map.Entry::getValue);
//
//        // 使用 Collections.max 方法找到最大的日期
//        Map.Entry<String, Date> maxEntry = Collections.max(map.entrySet(), comparator);
//        System.out.println("最大的日期：" + maxEntry.getKey() + " - " + maxEntry.getValue());
//
//        // 使用 Collections.min 方法找到最小的日期
//        Map.Entry<String, Date> minEntry = Collections.min(map.entrySet(), comparator);
//        System.out.println("最小的日期：" + minEntry.getKey() + " - " + minEntry.getValue());
//    }
}