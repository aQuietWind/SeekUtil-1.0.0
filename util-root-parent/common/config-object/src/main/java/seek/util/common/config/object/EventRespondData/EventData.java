package seek.util.common.config.object.EventRespondData;

import seek.util.common.config.object.UtilObject.Function.RunWithReturnParamFunction;
import seek.util.common.config.object.UtilObject.Function.RunWithReturnTwoParamFunction;
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
    private RunWithReturnParamFunction<Void,ValueData<T>> run;

    private EventData(){}

    public EventData(RunWithReturnTwoParamFunction<Boolean,T,T> check,RunWithReturnParamFunction<Void,ValueData<T>> run){
        this.check=check;
        this.run=run;
    }

    public boolean ok(T oldValue,T value){
        if (cooldownMillis > 0){
            if (System.currentTimeMillis() - lastUpdateTime > cooldownMillis)return false;
        }
        return check.run(oldValue,value);
    }

    public void run(ValueData<T> value){
        run.run(value);
    }
}
