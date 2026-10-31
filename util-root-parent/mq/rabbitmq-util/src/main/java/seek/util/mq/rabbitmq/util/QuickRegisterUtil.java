package seek.util.mq.rabbitmq.util;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import seek.util.common.config.object.mq.rabbitmq.ExchangeData;
import seek.util.common.config.object.mq.rabbitmq.QueueData;

import java.util.List;

public class QuickRegisterUtil{

    private void registerQueue(BeanDefinitionRegistry registry, List<ExchangeData> exchanges, List<QueueData> queues) {
        for (QueueData queue : queues) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();

        }
        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }

    public class Exchange{
        private ExchangeData exchangeData;
        private List<QueueData> queues;

        public Exchange(ExchangeData exchangeData, List<QueueData> queues) {
            this.exchangeData = exchangeData;
            this.queues = queues;
        }

    }


}