package com.github.maoxp.core.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MapCache
 *
 * @author mxp
 * @since JDK 1.8
 */
public class MapCacheUtil {
    private static final MapCacheUtil INS = new MapCacheUtil();

    /**
     * 默认存储1024个缓存
     */
    private static final int DEFAULT_CACHES = 1024;

    /**
     * 缓存容器
     */
    private final Map<String, CacheObject> cachePool;

    /**
     * 无参构造
     */
    public MapCacheUtil() {
        this(DEFAULT_CACHES);
    }

    /**
     * 有参构造
     *
     * @param cacheCount 缓存大小
     */
    public MapCacheUtil(int cacheCount) {
        cachePool = new ConcurrentHashMap<>(cacheCount);
    }

    /**
     * 单实例对象
     *
     * @return MapCacheUtil
     */
    public static MapCacheUtil builder() {
        return INS;
    }

    /**
     * 是否存在缓存key
     *
     * @param key 缓存key
     * @return boolean
     */
    public boolean existsKey(final String key) {
        return this.cachePool.containsKey(key);
    }

    /**
     * 读取String缓存
     *
     * @param key 缓存key
     * @return Object
     */
    public Object get(final String key) {
        CacheObject cacheObject = this.cachePool.get(key);
        if (null != cacheObject) {
            long cur = System.currentTimeMillis() / 1000;
            if (cacheObject.getExpired() <= 0 || cacheObject.getExpired() > cur) {
                return cacheObject.getValue();
            }
        }
        return null;
    }

    /**
     * 设置String缓存
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    public void set(final String key, final Object value) {
        this.set(key, value, -1);
    }

    /**
     * 设置String缓存并带过期时间
     *
     * @param key     缓存key
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    public void set(final String key, final Object value, long expired) {
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        cachePool.put(key, new CacheObject(key, value, expired));
    }

    /**
     * 根据key删除缓存
     *
     * @param key 缓存key
     */
    public void del(String key) {
        cachePool.remove(key);
    }

    /**
     * 清空缓存
     */
    public void clean() {
        this.cachePool.clear();
    }


    public static class CacheObject {
        private final String key;
        private final Object value;
        private final long expired;

        public CacheObject(String key, Object value, long expired) {
            this.key = key;
            this.value = value;
            this.expired = expired;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public long getExpired() {
            return expired;
        }
    }
}
