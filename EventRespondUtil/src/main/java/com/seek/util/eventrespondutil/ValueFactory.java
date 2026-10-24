package com.seek.util.eventrespondutil;

import com.seek.util.configobject.EventRespondData.ValueData;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;

public class ValueFactory {

    private ConcurrentHashMap<String, ValueData> valueMap = new ConcurrentHashMap<>();

    private final CheckPool checkPool;

    @Autowired
    public ValueFactory(CheckPool checkPool) {
        this.checkPool = checkPool;
    }

    public <T> ValueData<T> getValue(@Nonnull String key) {
        return valueMap.get(key);
    }

    public <T> boolean putValue(@Nonnull String key, @Nonnull ValueData<T> value) {
        if (valueMap.putIfAbsent(key, value) != null){
            throw new RuntimeException("this key already exists");
        }
        return true;
    }
    public boolean removeValue(@Nonnull String key) {
        return valueMap.remove(key) != null;
    }

    public <T> boolean setValueAndRespond(@Nonnull ValueData<T> valueData,@Nonnull T value){
        if (valueData.setValue(value)){
            checkPool.respond(valueData);
            return true;
        }
        return false;
    }



}
