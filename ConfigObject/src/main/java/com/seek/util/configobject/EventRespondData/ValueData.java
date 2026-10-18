package com.seek.util.configobject.EventRespondData;

import com.seek.util.configobject.UtilObject.Exception.BizException;
import com.seek.util.configobject.UtilObject.Exception.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValueData<T> {
    private T value;
    private T oldValue;
    private boolean changed = false;
    private boolean eventCheckedNow = false;
    private List<EventData<T>> selfishEventList = new ArrayList<>();
    private List<EventData<T>> selflessEventList = new ArrayList<>();

    public synchronized boolean setValue(T newValue) {
        if (eventCheckedNow) throw new BizException(ErrorCodeEnum.REQUEST_IN_COOLDOWN);
        if (newValue!=this.value&&!newValue.equals(this.value)) {
            this.oldValue = this.value;
            this.value = newValue;
        }
        this.changed = true;
        return true;
    }










}
