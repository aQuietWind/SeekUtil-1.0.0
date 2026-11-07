package seek.util.ai.langchain4j.util.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import seek.util.common.config.object.db.redis.RedisKeyData;


@ConfigurationProperties("seek.util.ai.langchain4j.config")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertiesConfig {
    private RedisSessionMemory redisSessionMemory=new RedisSessionMemory();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RedisSessionMemory{
        private Integer storeMax=20;
        private RedisKeyData redisKey=new RedisKeyData("seek:util:langchain4j:memory:", 604800000L);
    }

}
