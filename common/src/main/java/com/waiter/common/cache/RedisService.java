package com.waiter.common.cache;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.TimeUnit;

/**
 * @author lizhihui
 * @version 2018/11/1
 */
public class RedisService {
    /**
     * 定义一个用于操作string-string键值对的redis操作类
     */
    @Autowired
    private RedisTemplate<String,String> redisStringTemplate;

    /**
     * 定义一个用于操作string-session键值对的redis操作类
     */
    @Autowired
    private RedisTemplate<String,Session> redisSessionTemplate;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 将string-string键值对放入redis，不设置过期时间
     * @param key
     * @param value
     */
    public void setAllTimeStringRedis(String key, String value) {
        try {
            String setKey = key;
            redisStringTemplate.opsForValue().set(setKey, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将string-string键值对放入redis，并设置过期时间，单位为秒
     * @param key
     * @param value
     * @param ttl
     */
    public void setStringRedis(String key, String value, long ttl) {
        try {
            String setKey = key;
            redisStringTemplate.opsForValue().set(setKey, value);
            redisStringTemplate.expire(key, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将string-session键值对放入redis，并设置过期时间，单位为秒
     * @param key
     * @param value
     * @param ttl
     */
    public void setByteInRedis(String key, Session value, long ttl) {
        String setKey = key;
        redisSessionTemplate.opsForValue().set(setKey, value);
        redisSessionTemplate.expire(key, ttl, TimeUnit.SECONDS);
    }

    /**
     * 根据key值获取string-string键值对的value
     * @param key
     * @return
     */
    public String getStringInRedis(String key) {
        try {
            String getKey = key;
            String strValue = redisStringTemplate.opsForValue().get(getKey);
            return strValue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据key值获取string-session键值对的value
     * @param key
     * @return
     */
    public Session getByteInRedis(String key) {
        String getKey = key;
        Session session = redisSessionTemplate.opsForValue().get(getKey);
        return session;
    }

    /**
     * 根据key值删除键值对(好像用redisStringTemplate这个实例也可以删除string-session键值对)
     * @param key
     */
    public void deleteRedis(String key) {
        try {
            redisStringTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据key值获得缓存剩余有效时长
     * @param key
     * @returnnb
     */
    public long getTtlByKey(String key){
        return redisStringTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}
