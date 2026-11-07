package seek.util.cloud.sentinel.util.autoconfigure;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@org.springframework.context.annotation.Import(AutoConfig.class)
public @interface SentinelImport {}
