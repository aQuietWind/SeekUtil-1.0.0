package seek.util.common.config.object.db.redis;

import lombok.Data;

@Data
public class RedisStreamData {
    private String name;
    private String messageKey;
    private RedisConsumerData consumer;
}
