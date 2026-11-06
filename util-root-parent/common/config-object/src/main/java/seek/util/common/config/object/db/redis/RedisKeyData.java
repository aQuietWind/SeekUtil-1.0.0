package seek.util.common.config.object.db.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisKeyData {
    private String name;
    private Long durationMillis;
    public String getRedisKey(Object key){
        if (key==null||key.equals("")) return name;
        return name+key;
    }
}
