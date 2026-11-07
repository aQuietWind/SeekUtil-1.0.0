package seek.util.common.event.respond.system.support;

import seek.util.common.config.object.common.event.respond.system.EventData;
import seek.util.common.config.object.common.event.respond.system.ValueData;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import seek.util.common.event.respond.system.config.PropertiesConfig;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CheckPool {
    @Nonnull
    private final ExecutePool executePool;
    @Nonnull
    private final DelayPool delayPool;
    private final PropertiesConfig propertiesConfig;

    @Autowired
    public CheckPool(@Nonnull ExecutePool executePool, @Nonnull DelayPool delayPool, @Nonnull PropertiesConfig propertiesConfig) {
        this.executePool = executePool;
        this.delayPool = delayPool;
        this.propertiesConfig = propertiesConfig;
    }

    ThreadPoolExecutor executor;

    @PostConstruct
    public void init(){
        executor=new ThreadPoolExecutor(
                propertiesConfig.getCheckPool().getThreadCoreSize(),                         // core
                propertiesConfig.getCheckPool().getThreadMaxSize(),                         // max
                propertiesConfig.getCheckPool().getThreadAliveSeconds(),                       // 空闲时间
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                Thread::new,
                new ThreadPoolExecutor.CallerRunsPolicy()  // 拒绝策略
        );
    }

    public <T> void respond(ValueData<T> valueData){
        executor.submit(()->check(valueData));
    }

    public <T> void check(ValueData<T> valueData){
        ArrayList<EventData<T>> result=null;
        result=valueData.checkSelfishEvent();
        if (result==null){
            result= valueData.checkSelflessEvent();
            if (result==null)return;
            executePool.respond(result,valueData);
        }
        //如果是自私事件,则只执行一个
        executePool.respond(result.getFirst(),valueData);
    }










}
