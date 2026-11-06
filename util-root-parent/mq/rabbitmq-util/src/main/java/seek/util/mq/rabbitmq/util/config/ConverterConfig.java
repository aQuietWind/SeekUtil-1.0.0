package seek.util.mq.rabbitmq.util.config;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterConfig {

    private final PropertiesConfig propertiesConfig;

    public ConverterConfig(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }
    //序列化工具，用于对复杂的Object传递
    private String packageName;
    @PostConstruct
    public void init() {
        if (propertiesConfig.getConverterPackage()!=null&& !propertiesConfig.getConverterPackage().isEmpty()) {
            packageName = propertiesConfig.getConverterPackage();
        }
        else packageName="seek.util";
    }

    @Bean
    public MessageConverter messageConverter() {
        System.err.println(packageName);
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        //添加根包
        typeMapper.setTrustedPackages(packageName);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
