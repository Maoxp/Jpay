
package com.github.maoxp.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.CaseInsensitiveMap;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>JPay Http Response</p>
 *
 * @author Javen
 */
@Data
public class JPayHttpResponse implements Serializable {
    private static final long serialVersionUID = 6089103955998013402L;
    private String body;
    private byte[] bodyByte;
    private int status;
    private Map<String, List<String>> headers;

    public String getHeader(String name) {
        List<String> values = this.headerList(name);
        return CollUtil.isEmpty(values) ? null : values.get(0);
    }

    private List<String> headerList(String name) {
        if (CharSequenceUtil.isBlank(name)) {
            return Collections.emptyList();
        } else {
            CaseInsensitiveMap<String, List<String>> headersIgnoreCase = new CaseInsensitiveMap<>(getHeaders());
            return headersIgnoreCase.get(name.trim());
        }
    }

    @Override
    public String toString() {
        return "JPayHttpResponse{" +
                "body='" + body + '\'' +
                ", bodyByte=" + Arrays.toString(bodyByte) +
                ", status=" + status +
                ", headers=" + headers +
                '}';
    }
}
