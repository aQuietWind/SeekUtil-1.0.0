package com.seek.util.caffeineutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.seek.util.configobject.CaffeineData.CaffeineData;
import com.seek.util.configobject.RedisData.RedisKeyData;
import com.seek.util.configobject.UtilObject.Exception.BizException;
import com.seek.util.configobject.UtilObject.Exception.ErrorCodeEnum;
import com.seek.util.redisutil.RedisUtil;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;


public class JvmCaffeineParent<T,E> {
    public static final String caffeineFail="caffeineFailResult";
    private final RedisUtil redisUtil;
    private final RedisKeyData redisKey;
    private final CaffeineData setting;
    private static final ObjectMapper mapper;
    private final Class<E> resultClass;
    static{
        Jackson2ObjectMapperBuilder builder=new Jackson2ObjectMapperBuilder();
        mapper = builder.build();
        // 注册Java8时间序列化模块
        mapper.registerModule(new JavaTimeModule());
    }
    // 全局单例缓存（唯一实例）
    protected Cache<T, E> CACHE;

    public JvmCaffeineParent(RedisUtil redisUtil, Class<E> resultClass, RedisKeyData redisKey, CaffeineData setting) {
        this.redisUtil=redisUtil;
        this.resultClass=resultClass;
        this.redisKey=redisKey;
        this.setting=setting;
        defaultInit();
    }

    protected void defaultInit(){
        this.CACHE=Caffeine.newBuilder()
                .maximumSize(setting.getMaxSize())
                .expireAfterWrite(setting.getExpireSeconds(), TimeUnit.SECONDS)
                .recordStats()
                .build();
    }

    protected void defaultDestroy(){
        CACHE.cleanUp();
        CACHE.invalidateAll();
    }

    // 存缓存
    public void put(T key, E value) {
        CACHE.put(key, value);
    }
    //取缓存，没有返回 null
    public E get(T key) {
        return CACHE.getIfPresent(key);
    }
    // 取缓存，如果没有，自动执行 load 逻辑并写入缓存（最常用）
    public E get(T key, Function<T, E> loader) {
        return CACHE.get(key, loader);
    }
    //删除缓存
    public void delete(T key) {
        CACHE.invalidate(key);
    }
    //清空所有缓存
    public void clear() {
        CACHE.invalidateAll();
    }
    public Cache<T,E> getCACHE() {
        return CACHE;
    }




    //jvm-redis-mysql多级缓存逻辑方法
    public E getAndAutoLoad(T key,Function<T, E> loader){
        if (key == null||key.equals("")) return null;
        return CACHE.get(key,k->{
            //从redis中获取分布式缓存
            String json=redisUtil.getString(redisKey,key);
            //判断是否为空
            if (json!=null&& !json.isEmpty()){
                //判断是否为缓存穿透
                if (caffeineFail.equals(json))return null;
                //返回正确值
                try {
                    return mapper.readValue(json,resultClass);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            //从目标方法中获取值
            E result=loader.apply(key);
            //防止缓存穿透,在分布式场景下预先写入redis中，除此，可用额外缓存空值代替方案进行jvm处直接判断是否为缓存
            if (result==null){
                //切记不要误用set(String,String,long),这个会导致数据偏移，会造成实际字符串的不同,而且它和set(String,String,Duration)很像，也很容易误写
                redisUtil.trySetStringWithExpire(redisKey,key, caffeineFail);
                return null;
            }
            //不是缓存穿透则存储到redis做分布式缓存后返回
            try {
                redisUtil.justSetStringWithExpire(redisKey,key, mapper.writeValueAsString(result));
            }
            catch (JsonProcessingException e) {throw new RuntimeException(e);}
            return result;
        });
    }


    //jvm-redis-mysql库修改，缓存共同删除方法
    public void updateAndRemoveCaffeine(T key,Function<T, Boolean> loader) {
        if (key == null||key.equals("")) return;
        //执行修改或者删除操作，并且判断该操作是否成功
        if (!loader.apply(key)) throw new BizException(ErrorCodeEnum.DATA_NOT_FOUND);
        //redis清除缓存
        redisUtil.delete(redisKey,key);
        //jvm清除缓存
        CACHE.invalidate(key);
    }

    //删除redis-jvm缓存
    public void deleteAllCaffeine(T key) {
        if (key == null||key.equals("")) return;
        //redis清除缓存
        redisUtil.delete(redisKey,key);
        //jvm清除缓存
        CACHE.invalidate(key);
    }
}
