package seek.util.db.redis.util.autoconfigure;

import seek.util.db.redis.util.config.PropertiesConfig;
import seek.util.db.redis.util.util.Redisson.RedissonUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

//此处可能属于过度引入redisson，可以将redisson的工具封装拆出来，作为另外一个模块使用
@Configuration
public class RedissonAutoConfig {

    @Bean
    public RedissonClient redissonClient(PropertiesConfig propertiesConfig) {
        String host=propertiesConfig.getRedissonConnection().getHost();
        String port=propertiesConfig.getRedissonConnection().getPort();
        String password=propertiesConfig.getRedissonConnection().getPassword();
        //获取配置对象
        Config config = new Config();
        //设置地址和密码,常规的读取配置会读取空字符串，所以需要进行判断变为null值
        config.useSingleServer().setAddress("redis://"+host+":"+port).setPassword(password.isBlank()?null:password);
        //导入配置，并且返回工具对象给SpringBoot
        return Redisson.create(config);
    }

    @Bean
    public RedissonUtil redissonUtil(RedissonClient redissonClient) {
        return new RedissonUtil(redissonClient);
    }
}
