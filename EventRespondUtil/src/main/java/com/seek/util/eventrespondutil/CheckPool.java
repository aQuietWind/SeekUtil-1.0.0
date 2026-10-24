package com.seek.util.eventrespondutil;

import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CheckPool {

    @Value("${seek.util.event-respond.config.check-pool.thread-core-size}")
    private int threadCoreSize;
    @Value("${seek.util.event-respond.config.check-pool.thread-max-size}")
    private int threadMaxSize;
    @Value("${seek.util.event-respond.config.check-pool.thread-alive-seconds}")
    private int threadAliveSeconds;

    @Nonnull
    private final ExecutePool executePool;
    @Nonnull
    private final DelayPool delayPool;

    @Autowired
    public CheckPool(@Nonnull ExecutePool executePool, @Nonnull DelayPool delayPool) {
        this.executePool = executePool;
        this.delayPool = delayPool;
    }

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
