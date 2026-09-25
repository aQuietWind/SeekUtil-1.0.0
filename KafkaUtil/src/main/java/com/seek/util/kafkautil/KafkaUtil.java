package com.seek.util.kafkautil;

import com.seek.util.configobject.UtilObject.Exception.BizException;
import com.seek.util.configobject.UtilObject.Function.RunWithParam;
import com.seek.util.configobject.UtilObject.Function.RunWithTwoParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.Map;

@Slf4j
public class KafkaUtil {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    public KafkaUtil(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    //发送普通消息/
    public void sendMessage(String topic,Object msg) {
        kafkaTemplate.send(topic, msg);
    }

    //带key发送（相同key消息会分到同一个分区）
    public void sendMessageWithKey(String topic,String key, String msg) {
        kafkaTemplate.send(topic, key, msg);
    }

    //同步发送，获取发送结果并且同步执行操作
    public void sendSync(String topic, String msg, RunWithParam<SendResult<String,Object>> function) {
        try {
            // 等待发送完成
            function.function(kafkaTemplate.send(topic, msg).get());
        }
        catch (BizException bizE){throw bizE;}
        catch (Exception e) {
            log.error("kafka发送并执行回调任务时异常:",e);
            throw new RuntimeException(e);
        }
    }

    //发送并且执行回调
    public void sendWithCallBack(String topic, String msg, RunWithTwoParams<SendResult<String,Object>,Throwable> function) {
        kafkaTemplate.send(topic, msg).whenComplete(function::function);
    }

    //让多条消息事务发送
    public boolean sendInTransaction(Map<String,Object> msg){
        return kafkaTemplate.executeInTransaction(operations -> {
            for(Map.Entry<String,Object> entry:msg.entrySet()) sendMessage(entry.getKey(),entry.getValue());
            return true;
        });
    }

}
