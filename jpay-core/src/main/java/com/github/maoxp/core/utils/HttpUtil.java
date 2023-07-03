package com.github.maoxp.core.utils;

import com.github.maoxp.core.http.AbstractHttpDelegate;
import com.github.maoxp.core.http.DefaultHttp;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * http工具类
 */
public class HttpUtil {
    private HttpUtil() {
    }

    private static AbstractHttpDelegate delegate = new DefaultHttp();

    public static AbstractHttpDelegate getDelegate() {
        return delegate;
    }

    /**
     * 自定义 Http 客户端
     *
     * @param delegate http客户端
     */
    public static void setDelegate(AbstractHttpDelegate delegate) {
        HttpUtil.delegate = delegate;
    }

    /**
     * 快捷获取Request请求中的数据
     *
     * @param request http Request 请求
     * @return String
     */
    public static String readData(HttpServletRequest request) {
        try (BufferedReader br = request.getReader()) {
            StringBuilder result = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将同步通知的参数转化为Map
     *
     * @param request {@link HttpServletRequest}
     * @return 转化后的 Map
     */
    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }
}