package com.seek.util.redisutil;

import ch.qos.logback.core.util.TimeUtil;
import com.seek.util.configobject.RedisData.RedisKeyData;
import com.seek.util.configobject.UtilObject.Exception.BizException;
import com.seek.util.configobject.UtilObject.Exception.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
public class RedisUtil {
    public static final String cooldownValue="true";

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    //快速鉴别是否处于冷却期
    public void checkCooldown(RedisKeyData key, Object id){
        if (Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(
                key.getRedisKey(id)
                , cooldownValue,
                key.getDurationMillis(), TimeUnit.MICROSECONDS ))) throw new BizException(ErrorCodeEnum.REQUEST_IN_COOLDOWN);
    }

    //快速设置
    public boolean trySetStringWithExpire(RedisKeyData key,Object id,String value){
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key.getRedisKey(id), value, key.getDurationMillis(), TimeUnit.MICROSECONDS));
    }

    //快速设置
    public boolean trySetString(RedisKeyData key,Object id,String value){
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key.getRedisKey(id), value));
    }

    //快速设置
    public void justSetStringWithExpire(RedisKeyData key,Object id,String value){
        stringRedisTemplate.opsForValue().set(key.getRedisKey(id), value,key.getDurationMillis(), TimeUnit.MICROSECONDS);
    }


    //快速获取
    public String getString(RedisKeyData key,Object id){
        return stringRedisTemplate.opsForValue().get(key.getRedisKey(id));
    }

    //快速删除
    public boolean delete(RedisKeyData key,Object id){
        return stringRedisTemplate.delete(key.getRedisKey(id));
    }

    //快速自增
    public Long increase(RedisKeyData key,Object id,int value){
        return stringRedisTemplate.opsForValue().increment(key.getRedisKey(id), value);
    }

    //快速设置过期
    public boolean expire(RedisKeyData key,Object id){
        return stringRedisTemplate.expire(key.getRedisKey(id), key.getDurationMillis(), TimeUnit.MICROSECONDS);
    }

    //快速检查zset中某个value值是否存在
    public boolean zSetXIsExistByScore(RedisKeyData key,Object id,String value){
        return stringRedisTemplate.opsForZSet().score(key.getRedisKey(id), value)!=null;
    }

    public<T> Object doScript(DefaultRedisScript<T> defaultRedisScript,List<String> keys,Object ... args){
        return stringRedisTemplate.execute(defaultRedisScript         //执行脚本
                , keys       //KEYS参数
                , args);     //ARGV参数
    }








}
