package seek.util.common.event.respond.system.support;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import seek.util.common.event.respond.system.config.PropertiesConfig;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DelayPool {

    @Nonnull
    private final PropertiesConfig propertiesConfig;

    @Autowired
    public DelayPool(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }
    ThreadPoolExecutor executor;

    @PostConstruct
    public void init(){
        executor=new ThreadPoolExecutor(
                propertiesConfig.getDelayPool().getThreadCoreSize(),                         // core
                propertiesConfig.getDelayPool().getThreadMaxSize(),                         // max
                propertiesConfig.getDelayPool().getThreadAliveSeconds(),                       // 空闲时间
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                Thread::new,
                new ThreadPoolExecutor.CallerRunsPolicy()  // 拒绝策略
        );
    }
}
