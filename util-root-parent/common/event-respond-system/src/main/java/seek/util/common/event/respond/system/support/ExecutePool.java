package seek.util.common.event.respond.system.support;

import com.google.gson.Gson;
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

public class ExecutePool {
    private static final Gson gson = new Gson();

    ThreadPoolExecutor executor;

    @Nonnull
    private final DelayPool delayPool;
    @Nonnull
    private final PropertiesConfig propertiesConfig;

    @Autowired
    public ExecutePool(@Nonnull DelayPool delayPool, @Nonnull PropertiesConfig propertiesConfig) {
        this.delayPool = delayPool;
        this.propertiesConfig = propertiesConfig;
    }

    @PostConstruct
    public void init(){
        executor=new ThreadPoolExecutor(
                propertiesConfig.getExecutePool().getThreadCoreSize(),                         // core
                propertiesConfig.getExecutePool().getThreadMaxSize(),                         // max
                propertiesConfig.getExecutePool().getThreadAliveSeconds(),                       // 空闲时间
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
