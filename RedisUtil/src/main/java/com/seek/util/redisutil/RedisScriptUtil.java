package com.seek.util.redisutil;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

public class RedisScriptUtil {

    public static DefaultRedisScript<Boolean> luaQuickInit(String path){
        //初始化脚本对象
        DefaultRedisScript<Boolean> luaScript= new DefaultRedisScript<>();
        luaScript.setLocation(new ClassPathResource(path));  //设置Lua脚本地址，一般放于resources/Lua下
        luaScript.setResultType(Boolean.class);      //设置脚本返回值，与泛型保持一致
        return luaScript;
    }
}
