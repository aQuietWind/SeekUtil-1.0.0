package com.seek.util.redisutil.AutoConfig;

import com.seek.util.redisutil.RedisBitMapUtil;
import com.seek.util.redisutil.RedisIdUtil;
import com.seek.util.redisutil.RedisStreamUtil;
import com.seek.util.redisutil.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class UtilAutoConfig {
    @Bean
    @Lazy
    public RedisBitMapUtil redisBitMapUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisBitMapUtil(stringRedisTemplate);
    }
    @Bean
    @Lazy
    public RedisUtil redisUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisUtil(stringRedisTemplate);
    }
    @Bean
    @Lazy
    public RedisStreamUtil redisStreamUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisStreamUtil(stringRedisTemplate);
    }
    @Bean
    @Lazy
    public RedisIdUtil redisIdUtil(RedisUtil redisUtil) {
        return new RedisIdUtil(redisUtil);
    }
}
