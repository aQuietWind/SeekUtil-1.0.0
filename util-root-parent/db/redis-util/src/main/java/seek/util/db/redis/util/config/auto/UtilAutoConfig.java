package seek.util.db.redis.util.config.auto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import seek.util.db.redis.util.RedisBitMapUtil;
import seek.util.db.redis.util.RedisIdUtil;
import seek.util.db.redis.util.RedisStreamUtil;
import seek.util.db.redis.util.RedisUtil;

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
