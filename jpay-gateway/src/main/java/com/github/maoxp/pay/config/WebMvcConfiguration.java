package com.github.maoxp.pay.config;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.maoxp.core.utils.RedisUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * MvcConfiguration
 *
 * @author mxp
 * @date 2023/5/29年05月29日 15:14
 * @since JDK 1.8
 */
@Configuration
@ConditionalOnWebApplication(type = SERVLET)
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 自定义的格式化器
     * <p>支持 将请求参数的字符串值转换为目标类型，并将目标类型的值格式化为字符串，以便在响应中返回</p>
     * <ul>
     * <li>HH:mm:ss -> LocalTime</li>
     * <li>yyyy-MM-dd -> LocalDate</li>
     * <li>yyyy-MM-dd HH:mm:ss -> LocalDateTime</li>
     * </ul>
     *
     * @param registry 注册自定义的格式化器
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setTimeFormatter(DatePattern.NORM_TIME_FORMATTER);
        registrar.setDateFormatter(DatePattern.NORM_DATE_FORMATTER);
        registrar.setDateTimeFormatter(DatePattern.NORM_DATETIME_FORMATTER);
        registrar.registerFormatters(registry);
    }

    /**
     * 自定义Cors跨域
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")//允许跨域访问的路劲
                .allowCredentials(true)    //带上cookie信息
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders("*")
                .allowedOrigins("*")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //在前端显示的路径： 域名/profile/20220516173508528.png
        //registry.addResourceHandler("/profile/**").addResourceLocations("file:/app/Temp/");
    }

    /**
     * 自定义 http请求和响应数据消息转换器
     *
     * @param converters 消息转换器list
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //support Java 8 LocalDatetime
        final JavaTimeModule timeJacksonModule = new JavaTimeModule();
        timeJacksonModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DatePattern.NORM_DATETIME_FORMATTER));
        timeJacksonModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DatePattern.NORM_DATETIME_FORMATTER));

        // 构建 Jackson ObjectMapper 的类实例
        ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
                .locale(Locale.CHINA)
                .timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
                .simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN)
                .serializerByType(Long.class, ToStringSerializer.instance)
                .serializationInclusion(JsonInclude.Include.ALWAYS)  // 设置JsonInclude.Include.NON_NULL时, 且POJO对象的property值为null，则序列化后对应属性将不显示。
                .modulesToInstall(timeJacksonModule)
                .indentOutput(true)
                .build();
        objectMapper = getObjectMapper(objectMapper);

        // MappingJackson2HttpMessageConverter 是处理 HTTP 请求和响应中的 JSON 数据的消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        converters.add(0, messageConverter);     // 设置为第一个转换
    }

    /**
     * ObjectMapper 对象的操作
     *
     * @param mapper {@link ObjectMapper} Json数据与Java对象的转换器
     * @return {@link ObjectMapper}
     */
    private ObjectMapper getObjectMapper(ObjectMapper mapper) {
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // (默认)启用此功能，日期/时间被序列化为时间戳
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // (默认)启用此功能，如果遇到未知属性，将抛出JsonMappingException
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

        final String hget = RedisUtil.<String, String>hget("ss", "d");
        System.out.println(hget);
        return mapper;
    }
}
