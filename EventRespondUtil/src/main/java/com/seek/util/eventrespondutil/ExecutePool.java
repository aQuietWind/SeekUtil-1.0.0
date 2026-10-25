package com.seek.util.eventrespondutil;

import com.google.gson.Gson;
import com.seek.util.configobject.EventRespondData.EventData;
import com.seek.util.configobject.EventRespondData.ValueData;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutePool {
    @Value("${seek.util.event-respond.config.execute-pool.thread-core-size}")
    private int threadCoreSize;
    @Value("${seek.util.event-respond.config.execute-pool.thread-max-size}")
    private int threadMaxSize;
    @Value("${seek.util.event-respond.config.execute-pool.thread-alive-seconds}")
    private int threadAliveSeconds;

    private static final Gson gson = new Gson();

    ThreadPoolExecutor executor;

    @Nonnull
    private final DelayPool delayPool;

    @Autowired
    public ExecutePool(@Nonnull DelayPool delayPool) {
        this.delayPool = delayPool;
    }

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

    //执行传输的事件
    public<T> void respond(ArrayList<EventData<T>> eventDataList, ValueData<T> valueData){
        ValueData<T> copyValueData=copyValueData(valueData);
        for(EventData<T> eventData:eventDataList)executor.submit(()->executeEvent(eventData,copyValueData));
        valueData.setEventRespondNow(false);
    }
    public<T> void respond(EventData<T> eventData, ValueData<T> valueData){
        ValueData<T> copyValueData=copyValueData(valueData);
        executor.submit(()->executeEvent(eventData,copyValueData));
        valueData.setEventRespondNow(false);
    }

    private  <T> void executeEvent(EventData<T> eventData,ValueData<T> valueData){
        eventData.run(valueData);
    }

    private <T> ValueData<T> copyValueData(ValueData<T> originalValueData){
        return gson.fromJson(gson.toJson(originalValueData), ValueData.class);
    }

}
