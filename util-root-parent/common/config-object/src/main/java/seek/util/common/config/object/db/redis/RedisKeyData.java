package seek.util.common.config.object.db.redis;

import lombok.Data;

@Data
public class RedisKeyData {
    private String name;
    private Long durationMillis;
    public String getRedisKey(Object key){
        if (key==null||key.equals("")) return name;
        return name+key;
    }
}
