package com.github.maoxp.core.utils;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.maoxp.core.constants.CS;
import com.github.maoxp.core.exception.BizException;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * JacksonUtil
 *
 * @author mxp
 * @date 2023/6/5年06月05日 09:06
 * @since JDK 1.8
 */
@Slf4j
public final class JacksonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JacksonUtil jacksonUtil = new JacksonUtil();

    /**
     * 单例模式
     */
    private JacksonUtil() {
        //LocalDatetime序列化
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(CS.DateFormat.YMD)));
        timeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(CS.DateFormat.YMD_HMS)));
        timeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern(CS.DateFormat.YMD)));
        timeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(CS.DateFormat.YMD_HMS)));

        mapper.registerModule(timeModule);
        mapper.setTimeZone(TimeZone.getTimeZone(CS.DateFormat.TIME_ZONE));
        mapper.setDateFormat(new SimpleDateFormat(CS.DateFormat.YMD_HMS));
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static JacksonUtil builder() {
        return jacksonUtil;
    }

    public ObjectMapper objectMapper() {
        return mapper;
    }

    /**
     * 对象转json
     * <p>
     * 在转换过程中，有可能有的属性被设成空就不序列化等的需求，可以在类的属性上或直接在类上加上一下注解。
     * 用在属性上就是只针对一个属性，用在类上就是针对类里的所有属性。
     *
     * <ul>
     *     <li>@JsonInclude(Include.NON_NULL)</li>
     *     <li>@JsonInclude(Include.Include.ALWAYS) 默认 </li>
     *     <li>@JsonInclude(Include.NON_DEFAULT) 属性为默认值不序列化 </li>
     *     <li>@JsonInclude(Include.NON_EMPTY) 属性为 空（“”） 或者为 NULL 都不序列化 </li>
     *     <li>@JsonInclude(Include.NON_NULL) 属性为NULL 不序列化</li>
     * </ul>
     *
     * @param object 对象
     * @return json
     */
    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BizException("Jackson转换字符串（String）过程失败", e);
        }
    }

    /**
     * json转bean
     * <p>
     * 如果json中的字段比clazz字段多会报错，忽略多余的字段在clazz中添加下面注解。{@code @JsonIgnoreProperties(ignoreUnknown = true)}
     *
     * @param json  json
     * @param clazz Class
     * @param <T>   范型
     * @return 对象
     */
    public <T> T toBean(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new BizException("Jackson转换对象（Object）过程失败", e);
        }
    }

    /**
     * 字符串转换为List
     *
     * @param listStr       json
     * @param typeReference new TypeReference<List<Object>>() {}
     * @param <T>           T
     * @return T
     */
    public <T> T toBean(String listStr, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(listStr, typeReference);
        } catch (JsonProcessingException e) {
            throw new BizException("Jackson转换Object过程失败", e);
        }
    }
}