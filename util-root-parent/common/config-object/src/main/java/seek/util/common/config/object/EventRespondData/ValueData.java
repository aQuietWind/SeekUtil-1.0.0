package seek.util.common.config.object.EventRespondData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValueData<T> {
    private T value;
    private T oldValue;
    private boolean changed = false;
    private boolean eventRespondNow = false;
    private EventList<T> selfishEventList = new EventList<>();
    private EventList<T> selflessEventList = new EventList<>();


    private static final String CheckConflict="is checked event now";

    //设置值
    public synchronized boolean setValue(T newValue) {
        //正在执行事件时不能设置值
        if (eventRespondNow) throw new RuntimeException(CheckConflict);
        if (newValue!=this.value&&!newValue.equals(this.value)) {
            this.oldValue = this.value;
            this.value = newValue;
        }
        this.changed = true;
        return true;
    }

    //添加事件
    public synchronized boolean addEvent(EventData<T> eventData) {
        if (eventRespondNow) throw new RuntimeException(CheckConflict);
        if (eventData==null) throw new RuntimeException("event data is null");
        if (eventData.isSelfish()) {
            return selfishEventList.addEvent(eventData);
        }
        else {
            return selflessEventList.addEvent(eventData);
        }
    }

    //检查自私事件
    public synchronized ArrayList<EventData<T>> checkSelfishEvent() {
        return selfishEventList.checkList(oldValue,value);
    }

    //检查公共事件
    public synchronized ArrayList<EventData<T>> checkSelflessEvent() {
        return selflessEventList.checkList(oldValue,value);
    }











    }
