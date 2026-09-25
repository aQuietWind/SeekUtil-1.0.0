package com.seek.util.redisutil;

import com.seek.food.configobject.RedisData.RedisKeyData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class RedisIdUtil {
    private static final Random random=new Random();
    private final RedisUtil redisUtil;
    @Autowired
    public RedisIdUtil(RedisUtil redisUtil) {
        this.redisUtil=redisUtil;
    }

    public long IdGenerateByIncreaseRandom(RedisKeyData key, int randomNumberMax) {
        return redisUtil.increase(key,null,random.nextInt(randomNumberMax));
    }
    public long IdGenerateByIncrease(RedisKeyData key) {
        return redisUtil.increase(key,null,1);
    }
}














