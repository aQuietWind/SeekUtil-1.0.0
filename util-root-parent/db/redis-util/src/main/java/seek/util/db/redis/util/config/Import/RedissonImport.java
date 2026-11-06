package seek.util.db.redis.util.config.Import;

import org.springframework.context.annotation.Import;
import seek.util.db.redis.util.config.RedissonConfig;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedissonConfig.class)
public @interface RedissonImport {}
