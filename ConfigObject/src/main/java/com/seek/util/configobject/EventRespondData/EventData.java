package com.seek.util.configobject.EventRespondData;

import com.seek.util.configobject.UtilObject.Function.RunWithReturnParamFunction;
import com.seek.util.configobject.UtilObject.Function.RunWithReturnTwoParamFunction;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventData<T> {
    private long lastUpdateTime=-1;
    private long cooldownMillis=-1;
    private int order=1;
    private int urgentLevel=5;
    private int type=0;
    private boolean selfish=false;
    private RunWithReturnTwoParamFunction<Boolean,T,T> check;
    private RunWithReturnParamFunction<String,T> run;

    private EventData(){}

    public EventData(RunWithReturnTwoParamFunction<Boolean,T,T> check,RunWithReturnParamFunction<String,T> run){
        this.check=check;
        this.run=run;
    }

    public boolean ok(T oldValue,T value){
        if (cooldownMillis > 0){
            if (System.currentTimeMillis() - lastUpdateTime > cooldownMillis)return false;
        }
        return check.run(oldValue,value);
    }

    public String run(T value){
        return run.run(value);
    }
}
