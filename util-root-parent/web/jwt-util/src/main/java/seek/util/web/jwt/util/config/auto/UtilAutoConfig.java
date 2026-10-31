package seek.util.web.jwt.util.config.auto;

import seek.util.web.jwt.util.JWTUtil;
import seek.util.web.jwt.util.TokenUtil;
import seek.util.db.redis.util.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class UtilAutoConfig {
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
