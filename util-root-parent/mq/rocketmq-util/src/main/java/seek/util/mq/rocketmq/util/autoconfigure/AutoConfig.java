package seek.util.mq.rocketmq.util.autoconfigure;

import seek.util.mq.rocketmq.util.support.BizStore;
import seek.util.mq.rocketmq.util.support.DefaultTransactionListener;
import seek.util.mq.rocketmq.util.util.ProducerUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AutoConfig {
    @Bean
    @Lazy
    public BizStore bizStore() {
        return new BizStore();
    }
    @Bean
    @Lazy
    public DefaultTransactionListener defaultTransactionListener(BizStore bizStore) {
        return new DefaultTransactionListener(bizStore);
    }
    @Bean
    @Lazy
    public ProducerUtil producerUtil(RocketMQTemplate rocketMQTemplate,BizStore bizStore) {
        return new ProducerUtil(rocketMQTemplate,bizStore);
    }
}
