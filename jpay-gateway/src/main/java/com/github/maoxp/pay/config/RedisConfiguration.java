package com.github.maoxp.pay.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.maoxp.core.constants.CS;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * RedisConfiguration
 *
 * @author mxp
 * @date 2023/6/5年06月05日 16:57
 * @since JDK 1.8
 */
@Configuration
@EnableCaching
public class RedisConfiguration {
    //过期时间-1天
    private final Duration timeToLive = Duration.ofDays(-1);

    private Jackson2JsonRedisSerializer<Object> jsonRedisSerializer;

    /**
     * RedisTemplate 配置
     *
     * @param factory
     * @return
     */
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, ? extends Object> redisTemplate(RedisConnectionFactory factory) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(CS.DateFormat.YMD_HMS)));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(CS.DateFormat.YMD_HMS)));

        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);  // 指定要序列化的域，filed，get和set、Any是包括private 和public
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        om.registerModule(javaTimeModule);  // support java 8 localDateTime

        // 配置序列化器 解析（Object.class）任意对象
        jsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jsonRedisSerializer.setObjectMapper(om);    // 采用 Jackson的ObjectMapper 进行序列化和反序列化操作

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setDefaultSerializer(jsonRedisSerializer);    // 设置默认的序列化器为 Jackson2JsonRedisSerializer
        redisTemplate.setKeySerializer(keySerializer());        // 单独设置 key｜hashKey的序列化器 为 StringRedisSerializer
        redisTemplate.setHashKeySerializer(keySerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 定义 RedisCacheManager bean
     * <br/>
     * <p>
     * 参考spring boot 整合Redis配置<a href ="https://blog.csdn.net/cf082430/article/details/110297015">CacheManager</a> 实现缓存
     * </p>
     *
     * @param connectionFactory RedisConnectionFactory
     * @return {@link RedisCacheManager}
     */
    @Bean(name = "myCacheManager")
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //默认1
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(timeToLive)   // 设置缓存的默认过期时间
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jacksonSerializer()))
                .disableCachingNullValues();    // 不缓存空值

        // 根据redis缓存配置和reid连接工厂生成redis缓存管理器
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }

    /**
     * key类型的序列化器 StringRedisSerializer
     *
     * @return {@link StringRedisSerializer}
     */
    private RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    /**
     * 值采用GenericJackson2JsonRedisSerializer序列化
     *
     * @return {@link GenericJackson2JsonRedisSerializer}
     */
    private RedisSerializer<Object> genericJacksonSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    /**
     * 值采用Jackson2JsonRedisSerializer序列化
     *
     * @return {@link Jackson2JsonRedisSerializer}
     */
    private RedisSerializer<Object> jacksonSerializer() {
        return this.jsonRedisSerializer;
    }

    /**
     * 重新定义KeyGenerator.
     *
     * @return {@link KeyGenerator}
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName()).append(".");
                sb.append(method.getName()).append(":");
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

}
