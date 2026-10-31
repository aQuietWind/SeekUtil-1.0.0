package seek.util.db.redis.util;

import seek.util.common.config.object.RedisData.RedisKeyData;
import seek.util.common.config.object.UtilObject.Exception.BizException;
import seek.util.common.config.object.UtilObject.Exception.ErrorCodeEnum;
import seek.util.common.config.object.UtilObject.Function.RunFunction;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

public class RedissonUtil {

    private final RedissonClient redissonClient;
    public RedissonUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void lock(RedisKeyData key, Object id, RunFunction function) {
        RLock lock = redissonClient.getLock(key.getRedisKey(id));
        if (lock.tryLock()){
            try {
                function.run();
                return;
            }finally {
                lock.unlock();
            }
        }
        throw new BizException(ErrorCodeEnum.REQUEST_CONFLICT);
    }
}
