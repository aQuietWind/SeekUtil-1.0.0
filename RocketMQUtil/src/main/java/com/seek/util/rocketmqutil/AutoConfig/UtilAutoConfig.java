package com.seek.util.rocketmqutil.AutoConfig;

import com.seek.util.rocketmqutil.BizStore;
import com.seek.util.rocketmqutil.DefaultTransactionListener;
import com.seek.util.rocketmqutil.ProducerUtil;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class UtilAutoConfig {
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
