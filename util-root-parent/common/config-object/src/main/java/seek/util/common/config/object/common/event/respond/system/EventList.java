package seek.util.common.config.object.common.event.respond.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventList<T> {
    private ArrayList<EventData<T>> eventList=new ArrayList<>();
    private boolean needSort=false;

    public boolean addEvent(EventData<T> eventData){
        if (eventData==null) throw new NullPointerException("eventData is null");
        if (eventList.contains(eventData))return false;
        eventList.add(eventData);
        //方便后续排序
        needSort=true;
        return true;
    }

    public ArrayList<EventData<T>> checkList(T oldValue,T newValue){
        if (eventList.isEmpty()) return null;
        //先排序
        sort();
        boolean needList=false;
        ArrayList<EventData<T>> eventDataList=null;
        for (EventData<T> eventData : eventList) {
            if (eventData.ok(oldValue,newValue)){
                if (!needList) {
                    needList = true;
                    //需要再创建
                    eventDataList = new ArrayList<>();
                }
                eventDataList.add(eventData);
            }
        }
        return eventDataList;
    }

    public void sort(){
        if (needSort){
            eventList.sort(Comparator.comparingInt(EventData::getOrder));
            needSort=false;
        }
    }
}
