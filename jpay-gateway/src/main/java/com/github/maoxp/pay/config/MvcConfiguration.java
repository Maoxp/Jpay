package com.github.maoxp.pay.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.github.maoxp.core.constants.CS;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * MvcConfiguration
 *
 * @author mxp
 * @date 2023/5/29年05月29日 15:14
 * @since JDK 1.8
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new WxPayInterceptor()).addPathPatterns("/wxPay/**");
//        super.addInterceptors(registry);
    }
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //support Java 8 LocalDatetime
        final JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(CS.DateFormat.YMD_HMS)));
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(CS.DateFormat.YMD_HMS)));

        // 设置全局的时间转化，转换格式和时区。
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                // 设置JsonInclude.Include.NON_NULL时, 且POJO对象的property值为null，则序列化后对应属性将不显示。
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                .indentOutput(true)
                .dateFormat(new SimpleDateFormat(CS.DateFormat.YMD_HMS))
                .timeZone(TimeZone.getTimeZone(CS.DateFormat.TIME_ZONE))
                .modulesToInstall(timeModule);

        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter(builder.build());
        ObjectMapper mapper = getObjectMapper(messageConverter);
        messageConverter.setObjectMapper(mapper);
        // 设置为第一个转换
        converters.add(0, messageConverter);
    }

    /**
     * 自定义 jackson 序列化时，对象属性的操作
     *
     * @param messageConverter {@link MappingJackson2HttpMessageConverter} 消息转换器
     * @return {@link ObjectMapper}
     */
    private ObjectMapper getObjectMapper(MappingJackson2HttpMessageConverter messageConverter) {
        ObjectMapper mapper = messageConverter.getObjectMapper();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        // 设置序列时，long变成string
        mapper.registerModule(simpleModule);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 禁用 SerializationFeature.WRITE_DATES_AS_TIMESTAMPS(默认情况下启用此功能，因此默认情况下日期/时间被序列化为时间戳。)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // 禁用 DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES，默认情况下启用此功能（这意味着如果遇到未知属性，将抛出JsonMappingException)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // Jackson自定义序列化null值处理
        // 为mapper注册一个带有SerializerModifier的Factory，此modifier主要做的事情为：当序列化类型为array，list、set时，当值为空时，序列化成[]
        mapper.setSerializerFactory(mapper.getSerializerFactory().withSerializerModifier(new BeanSerializerModifier() {
            /**
             * 最重要的一个东西就是BeanPropertyWriter 这个类，这个类是由SerializerFactory 工厂进行实例化的
             * 其作用是对bean中的每个字段进行jackson操作的封装，其中封装了字段的一些元信息
             */
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                             List<BeanPropertyWriter> beanProperties) {
                for (int i = 0; i < beanProperties.size(); i++) {
                    BeanPropertyWriter writer = beanProperties.get(i);
                    writer.assignNullSerializer(new JsonSerializer<Object>() {
                        @Override
                        public void serialize(Object param, JsonGenerator jsonGenerator,
                                              SerializerProvider paramSerializerProvider) throws IOException {
                            if (isArrayType(writer)) {
                                // 处理list、array、set类型的null值
                                jsonGenerator.writeStartArray();
                                jsonGenerator.writeEndArray();
                            } else if (isNumberType(writer)) {
                                // 处理Integer、Long为null
                                jsonGenerator.writeNull();
                            } else if (isBoolType(writer)) {
                                // 将Boolean类型的null转成false
                                jsonGenerator.writeBoolean(false);
                            } else {
                                // 处理字符串等类型的null值
                                jsonGenerator.writeString("");
                            }
                        }
                    });
                }
                return beanProperties;
            }

            protected boolean isArrayType(BeanPropertyWriter writer) {
                final JavaType type = writer.getType();
                return type.isArrayType() || type.isTypeOrSubTypeOf(Collection.class);
            }

            protected boolean isBoolType(BeanPropertyWriter writer) {
                final JavaType type = writer.getType();
                return type.isTypeOrSubTypeOf(Boolean.class);
            }

            protected boolean isNumberType(BeanPropertyWriter writer) {
                final JavaType type = writer.getType();
                // `isTypeOrSubTypeOf`方法来检查该类型是否是 Number 类型或其子类
                return type.isTypeOrSubTypeOf(Number.class);
            }
        }));

        return mapper;
    }
}
