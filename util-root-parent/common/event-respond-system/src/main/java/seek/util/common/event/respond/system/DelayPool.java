package seek.util.common.event.respond.system;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DelayPool {

    @Value("${seek.util.event-respond.config.delay-pool.thread-core-size}")
    private int threadCoreSize;
    @Value("${seek.util.event-respond.config.delay-pool.thread-max-size}")
    private int threadMaxSize;
    @Value("${seek.util.event-respond.config.delay-pool.thread-alive-seconds}")
    private int threadAliveSeconds;

    ThreadPoolExecutor executor;

    @PostConstruct
    public void init(){
        executor=new ThreadPoolExecutor(
                threadCoreSize,                         // core
                threadMaxSize,                         // max
                threadAliveSeconds,                       // 空闲时间
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                Thread::new,
                new ThreadPoolExecutor.CallerRunsPolicy()  // 拒绝策略
        );
    }
}
