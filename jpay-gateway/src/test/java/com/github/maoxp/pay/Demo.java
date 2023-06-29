package com.github.maoxp.pay;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

/**
 * Demo
 *
 * @author mxp
 * @date 2023/6/29年06月29日 13:53
 * @since JDK 1.8
 */
public class Demo {
    @Test
    void testDemo() {
        final DateTime dateTime = DateUtil.date(System.currentTimeMillis() + 1000 * 60 * 3);
        String timeExpire = dateTime.toString("yyyy-MM-dd'T'HH:mm:ssXXX");
        System.out.println(timeExpire);
    }
}
