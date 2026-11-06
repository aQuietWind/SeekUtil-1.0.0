package seek.util.ai.langchain4j.util.config;

import jakarta.annotation.PostConstruct;
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
    private RedisSessionMemory redisSessionMemory;

    @PostConstruct
    public void init(){
        if(redisSessionMemory==null){
            redisSessionMemory=RedisSessionMemory.DEFAULT_CONFIG;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RedisSessionMemory{
        private Integer storeMax;
        private RedisKeyData redisKey;
        private static final Integer DEFAULT_STORE_MAX=20;
        private static final RedisKeyData DEFAULT_REDIS_KEY=new RedisKeyData("seek:util:langchain4j:memory:", 604800000L);
        private static final RedisSessionMemory DEFAULT_CONFIG=new RedisSessionMemory(DEFAULT_STORE_MAX,DEFAULT_REDIS_KEY);
    }

}
