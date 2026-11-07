package seek.util.db.redis.util.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import seek.util.db.redis.util.config.PropertiesConfig;
import seek.util.db.redis.util.util.Redis.RedisBitMapUtil;
import seek.util.db.redis.util.util.Redis.RedisIdUtil;
import seek.util.db.redis.util.util.Redis.RedisStreamUtil;
import seek.util.db.redis.util.util.Redis.RedisUtil;

@Configuration
public class AutoConfig {
    @Bean
    public PropertiesConfig propertiesConfig(){
        return new PropertiesConfig();
    }

    @Bean
    public RedisBitMapUtil redisBitMapUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisBitMapUtil(stringRedisTemplate);
    }
    @Bean
    public RedisUtil redisUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisUtil(stringRedisTemplate);
    }
    @Bean
    public RedisStreamUtil redisStreamUtil(StringRedisTemplate stringRedisTemplate) {
        return new RedisStreamUtil(stringRedisTemplate);
    }
    @Bean
    public RedisIdUtil redisIdUtil(RedisUtil redisUtil) {
        return new RedisIdUtil(redisUtil);
    }
}
