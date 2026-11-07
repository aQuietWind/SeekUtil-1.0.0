package seek.util.web.jwt.util.autoconfigure;

import seek.util.web.jwt.util.util.JWTUtil;
import seek.util.web.jwt.util.util.TokenUtil;
import seek.util.db.redis.util.util.Redis.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AutoConfig {
    @Bean
    @Lazy
    public JWTUtil jwtUtil() {
        return new JWTUtil();
    }
    @Bean
    @Lazy
    public TokenUtil tokenUtil(RedisUtil redisUtil,JWTUtil jwtUtil) {
        return new TokenUtil(redisUtil,jwtUtil);
    }
}
