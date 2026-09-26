package com.seek.util.jwtutil.AutoConfig;

import com.seek.util.jwtutil.JWTUtil;
import com.seek.util.jwtutil.TokenUtil;
import com.seek.util.redisutil.RedisUtil;
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
