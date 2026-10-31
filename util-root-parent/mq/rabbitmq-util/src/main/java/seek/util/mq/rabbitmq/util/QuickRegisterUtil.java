package seek.util.mq.rabbitmq.util;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import seek.util.common.config.object.mq.rabbitmq.ExchangeData;
import seek.util.common.config.object.mq.rabbitmq.QueueData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuickRegisterUtil{

    private static final HashMap<String, Exchange> exchanges = new HashMap<>();
    private static final HashMap<String, Queue>  queues = new HashMap<>();
    private static final ArrayList<Binding> binds = new ArrayList<>();

    private final QueueUtil queueUtil;

    public QuickRegisterUtil(QueueUtil queueUtil){
        this.queueUtil = queueUtil;
    }

    public void registerAll(BeanDefinitionRegistry registry, List<ExchangeData> exchangeDatas, List<QueueData> queueDatas) {
        //校验交换机集合
        if(exchangeDatas==null || exchangeDatas.isEmpty())throw new IllegalArgumentException("exchangeDatas is empty");
        //校验队列绑定集合
        if(queueDatas==null || queueDatas.isEmpty())throw new IllegalArgumentException("queueDatas is empty");
        //批量添加交换机
        for (ExchangeData exchangeData : exchangeDatas) {
            //校验交换机
            if (exchangeData==null||exchangeData.getExchangeName()==null||exchangeData.getType()==null){
                throw new IllegalArgumentException("a exchangeData is empty");
            }
            //生成交换机
            Exchange exchange = exchanges.get(exchangeData.getExchangeName());
            if (exchange==null)throw new IllegalArgumentException("exchange not right");
            //存放交换机
            exchanges.putIfAbsent(exchangeData.getExchangeName(),exchange );
        }
        //批量添加队列并且添加绑定
        for (QueueData queueData : queueDatas) {
            if (queueData==null||queueData.getQueueName()==null||queueData.getExchangeName()==null){
                throw new IllegalArgumentException("a queueData is empty");
            }
            //获取并存放队列
            Queue queue = queueUtil.generateQuorumQueue(queueData);
            queues.putIfAbsent(queueData.getQueueName(),queue);
            //从集合拿出队列和交换机进行绑定
            binds.add(BindingBuilder
                    .bind(queues.get(queueData.getQueueName()))
                    .to(exchanges.get(queueData.getExchangeName()))
                    .with(queueData.getRoutingKey()).noargs());
        }
        addBean(registry);
    }

    public Exchange createExchange(ExchangeData exchangeData) {
        if (exchangeData.getType().equals(ExchangeData.Direct)) return new DirectExchange(exchangeData.getExchangeName());
        else if (exchangeData.getType().equals(ExchangeData.Topic)) return new TopicExchange(exchangeData.getExchangeName());
        else if (exchangeData.getType().equals(ExchangeData.Fanout)) return new FanoutExchange(exchangeData.getExchangeName());
        else return null;
    }

    public void addBean(BeanDefinitionRegistry registry){
        //注册交换机
        exchanges.forEach((s,e)->{
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Exchange.class,()->e);
            registry.registerBeanDefinition(s, builder.getBeanDefinition());
        });
        //注册队列
        queues.forEach((s,e)->{
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Queue.class,()->e);
            registry.registerBeanDefinition(s, builder.getBeanDefinition());
        });
        //注册binding
        for (Binding binding : binds) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Binding.class,()->binding);
            registry.registerBeanDefinition(
                    binding.getExchange()+":"+binding.getDestination()+":"+binding.getRoutingKey()
                    , builder.getBeanDefinition());
        }
    }










}