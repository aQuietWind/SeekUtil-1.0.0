package seek.util.mq.rabbitmq.util.util;

import jakarta.annotation.Nonnull;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import seek.util.common.config.object.mq.rabbitmq.BindingData;
import seek.util.common.config.object.mq.rabbitmq.ExchangeData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 快速注册队列、交换机与绑定关系。
 * <p>
 * 将所有声明打包为一个 {@link Declarables} bean 注册进容器。
 * 由于 RabbitAdmin 启动时会收集容器中所有 {@link Declarable} 类型的 bean
 * （含 Declarables，会自动展开）并按 交换机 -> 队列 -> 绑定 的顺序自动 declare，
 * 因此注册后的效果与逐个 @Bean 声明 Queue/Exchange/Binding 完全一致：
 * 自动创建交换机、创建队列并完成绑定。
 */

public class RegisterUtil {

    private final QueueUtil queueUtil;

    @Autowired
    public RegisterUtil(QueueUtil queueUtil) {
        this.queueUtil = queueUtil;
    }

    /**
     * 批量注册交换机、队列与绑定关系（注册为单个 Declarables bean）
     *
     * @param registry              bean 定义注册器
     * @param declarableBeanName   Declarables bean 的名称，需保证容器内唯一
     * @param exchangeDatas         交换机数据集合
     * @param bindingDatas            队列数据集合（含与交换机的绑定关系）
     */
    private void registerAll(BeanDefinitionRegistry registry, String declarableBeanName,
                            List<ExchangeData> exchangeDatas, List<BindingData> bindingDatas) {
        Map<String, Exchange> exchanges = new LinkedHashMap<>();
        Map<String, Queue> queues = new LinkedHashMap<>();
        List<Binding> binds = new ArrayList<>();

        //预先批量添加交换机
        for (ExchangeData exchangeData : exchangeDatas) addExchange(exchanges,exchangeData);

        //批量添加队列,交换机并且添加绑定
        for (BindingData binding : bindingDatas)addBinding(exchanges,queues,binds,binding);

        // bean 注册，RabbitAdmin 会自动展开并按序声明
        registry.registerBeanDefinition(declarableBeanName,
                BeanDefinitionBuilder.genericBeanDefinition(Declarables.class, ()->generateDeclarables(exchanges,queues,binds))
                        .getBeanDefinition());
    }


    //生成交换机
    private Exchange createExchange(ExchangeData exchangeData) {
        if (exchangeData.getType()==null || exchangeData.getType().equals(ExchangeData.Direct)) return new DirectExchange(exchangeData.getExchangeName());
        else if (exchangeData.getType().equals(ExchangeData.Topic)) return new TopicExchange(exchangeData.getExchangeName());
        else if (exchangeData.getType().equals(ExchangeData.Fanout)) return new FanoutExchange(exchangeData.getExchangeName());
        //最后默认返回定向交换机
        else return new DirectExchange(exchangeData.getExchangeName());
    }

    //添加交换机于集合
    private void addExchange(Map<String, Exchange> exchanges,ExchangeData exchangeData) {
        //校验交换机
        if (exchangeData == null || exchangeData.getExchangeName() == null) throw new IllegalArgumentException("a exchangeData is empty");
        //生成交换机,并存放交换机
        if (!exchanges.containsKey(exchangeData.getExchangeName())) exchanges.putIfAbsent(exchangeData.getExchangeName(), createExchange(exchangeData));
    }

    //对一个Binding关系进行处理
    private void addBinding(Map<String, Exchange> exchanges,Map<String, Queue> queues,List<Binding> binds,BindingData binding) {
        //校验Binding是否合规
        if (binding == null || binding.getQueueName() == null || binding.getExchange() == null) throw new IllegalArgumentException("a queueData is empty");
        //存放交换机
        addExchange(exchanges,binding.getExchange());
        //获取并存放队列
        if (!queues.containsKey(binding.getQueueName())) queues.putIfAbsent(binding.getQueueName(), queueUtil.generateQuorumQueue(binding.getQueueName()));
        //从集合拿出队列和交换机进行绑定
        binds.add(BindingBuilder
                .bind(queues.get(binding.getQueueName()))
                .to(exchanges.get(binding.getExchange().getExchangeName()))
                .with(binding.getRoutingKey()).noargs());
    }

    //生成Declarables进行装载
    private Declarables generateDeclarables(Map<String, Exchange> exchanges,Map<String, Queue> queues,List<Binding> binds) {
        //打包为单个 Declarable集合,按照exchange->queue->binding的顺序
        List<Declarable> all = new ArrayList<>(exchanges.values());
        all.addAll(queues.values());
        all.addAll(binds);
        return new Declarables(all);
    }

    public BeanDefinitionRegistryPostProcessor quickRegistrar(String declarableBeanName,
                                                              List<ExchangeData> exchangeDatas, List<BindingData> bindingDatas){
        return new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(@Nonnull BeanDefinitionRegistry registry) throws BeansException {
                registerAll(registry,declarableBeanName, exchangeDatas, bindingDatas);
            }

            @Override
            public void postProcessBeanFactory(@Nonnull ConfigurableListableBeanFactory beanFactory) throws BeansException {
                BeanDefinitionRegistryPostProcessor.super.postProcessBeanFactory(beanFactory);
            }
        };
    }
}
