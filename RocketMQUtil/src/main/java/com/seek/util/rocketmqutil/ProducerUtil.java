package com.seek.util.rocketmqutil;

import com.seek.util.configobject.UtilObject.Exception.BizException;
import com.seek.util.configobject.UtilObject.Function.RunWithParam;
import com.seek.util.configobject.UtilObject.Function.RunWithReturnFunction;
import com.seek.util.configobject.UtilObject.Function.RunWithReturnParamFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProducerUtil {

    private final RocketMQTemplate rocketMQTemplate;
    private final BizStore bizStore;
    @Autowired
    public ProducerUtil(RocketMQTemplate rocketMQTemplate, BizStore bizStore) {
        this.rocketMQTemplate = rocketMQTemplate;
        this.bizStore = bizStore;
    }


    //普通同步消息（最常用，发送完等待broker返回结果）
    public SendResult sendSync(String topic, String tag, Object msg){
        return rocketMQTemplate.syncSend(getDestination(topic,tag), msg);
    }

    //异步消息，带回调
    public void asyncSend(String topic, String tag, Object msg, RunWithParam<SendResult> success,RunWithParam<Throwable> fail) {
        rocketMQTemplate.asyncSend(getDestination(topic,tag),msg,new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                success.function(sendResult);
            }
            @Override
            public void onException(Throwable throwable) {
                log.error("rocketmq发送异步消息时出现异常:", throwable);
                fail.function(throwable);
            }
        });
    }

    //单向消息：只管发，不等应答，日志采集场景，可靠性低
    public void sendOneWay(String topic, String tag, Object msg) {
        rocketMQTemplate.sendOneWay(getDestination(topic,tag), msg);
    }

    //延时消息  等级1~18，不能自定义毫秒时间 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
    public void sendDelayLevel(String topic, String tag, Object msg, int delayLevel) {
        // 延迟等级3 → 10秒
        rocketMQTemplate.syncSend(getDestination(topic,tag), MessageBuilder.withPayload(msg).build(), 3000, delayLevel);
    }

    //只能用于72h以内的延时
    public void sendDelay(String topic,String tag, Object message,long delaySeconds){
        try {
            Message msg = new Message(topic, tag, message.toString().getBytes(RemotingHelper.DEFAULT_CHARSET));
            msg.setDelayTimeSec(delaySeconds);
            rocketMQTemplate.getProducer().send(msg);
        }
        catch (BizException bizException) {
            throw bizException;
        }
        catch (Exception e){
            log.error("rocketmq发送自定义延时消息时出现异常:",e);
            throw new RuntimeException(e);
        }
    }

    //批量发送
    public SendResult sendBatchSync(String topic, String tag, Object ... msg) {
        List<Message> msgList = new ArrayList<>();
        for (Object obj : msg) {
            msgList.add(new Message(topic, tag, obj.toString().getBytes()));
        }
        // 同步批量发送
        return rocketMQTemplate.syncSend(getDestination(topic,tag), msgList);
    }

    //顺序发送
    public SendResult sendOrderlySync(String topic, String tag, Object msg,String hashKey) {
        // 同步顺序发送
        return rocketMQTemplate.syncSendOrderly(getDestination(topic,tag), msg,hashKey);
    }

    //批量顺序发送
    public SendResult sendBatchOrderlySync(String topic, String tag,String hashKey, Object ... msg) {
        List<Message> msgList = new ArrayList<>();
        for (Object obj : msg) {
            msgList.add(new Message(topic, tag, obj.toString().getBytes()));
        }
        // 同步批量顺序发送
        return rocketMQTemplate.syncSendOrderly(getDestination(topic,tag), msgList,hashKey);
    }

    //事务发送
    public SendResult sendInTransaction(String topic, String tag, Object msg, String bizCode, String bizKey
                                        , RunWithReturnFunction<Boolean> biz
                                        , RunWithReturnParamFunction<Boolean, String> bizCheck) {
        //预先判断
        if (biz==null) return sendSync(topic,tag,msg);
        //存放业务方法
        bizStore.putBiz(bizCode, biz);
        if (bizCheck!=null)bizStore.putBizCheck(bizCode, bizCheck);
        //事务发送
        return rocketMQTemplate.sendMessageInTransaction(
                getDestination(topic,tag),
                MessageBuilder.withPayload(msg).setHeader(RocketMQHeaders.KEYS,bizStore.codeConcatenateKey(bizCode,bizKey)).build()
                ,bizCode);
    }

    //拼接地址
    public String getDestination(String topic,String tag) {
        return topic + ":" + tag;
    }

}