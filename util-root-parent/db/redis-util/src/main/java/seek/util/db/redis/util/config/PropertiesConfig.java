package seek.util.db.redis.util.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("seek.util.db.redis.config")
public class PropertiesConfig {
    private RedisConnection redissonConnection=new RedisConnection();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RedisConnection {
        private String host="localhost";
        private String port="6379";
        private String password="123456";
    }
}
