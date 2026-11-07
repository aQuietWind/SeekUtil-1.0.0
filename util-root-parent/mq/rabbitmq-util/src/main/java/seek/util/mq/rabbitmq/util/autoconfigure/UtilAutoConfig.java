package seek.util.mq.rabbitmq.util.autoconfigure;

import seek.util.mq.rabbitmq.util.configuration.ConverterConfig;
import seek.util.mq.rabbitmq.util.configuration.ErrorExchangeConfig;
import seek.util.mq.rabbitmq.util.configuration.MessageReturnCallBackConfig;
import seek.util.mq.rabbitmq.util.configuration.PropertiesConfig;
import seek.util.mq.rabbitmq.util.util.CorrleationUtil;
import seek.util.mq.rabbitmq.util.util.MQUtil;
import seek.util.mq.rabbitmq.util.util.QueueUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import seek.util.mq.rabbitmq.util.util.RegisterUtil;

//@Configuration
public class UtilAutoConfig {

    @Bean
    public CorrleationUtil corrleationUtil() {
        return new CorrleationUtil();
    }
    @Bean
    public MQUtil mqUtil(RabbitTemplate rabbitTemplate,CorrleationUtil corrleationUtil) {
        return new MQUtil(rabbitTemplate,corrleationUtil);
    }
    @Bean
    public QueueUtil queueUtil() {
        return new QueueUtil();
    }
    @Bean
    public RegisterUtil quickRegisterUtil(QueueUtil queueUtil) {
        return new RegisterUtil(queueUtil);
    }
    @Bean
    public PropertiesConfig propertiesConfig(){
        return new PropertiesConfig();
    }
    @Bean
    public ConverterConfig converterConfig(PropertiesConfig propertiesConfig) {
        return new ConverterConfig(propertiesConfig);
    }
    @Bean
    public ErrorExchangeConfig errorExchangeConfig(QueueUtil queueUtil,RabbitTemplate rabbitTemplate,PropertiesConfig propertiesConfig) {
        return new ErrorExchangeConfig(queueUtil,rabbitTemplate,propertiesConfig);
    }
    @Bean
    public MessageReturnCallBackConfig messageReturnCallBackConfig() {
        return new MessageReturnCallBackConfig();
    }
    public static String beanName(Class<?> clazz) {
        return clazz.getName();
    }
}
