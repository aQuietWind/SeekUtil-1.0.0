package seek.util.mq.rabbitmq.util.autoconfigure;

import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import seek.util.common.config.object.mq.rabbitmq.BindingData;
import seek.util.common.config.object.mq.rabbitmq.ExchangeData;
import seek.util.mq.rabbitmq.util.configuration.ConverterConfig;
import seek.util.mq.rabbitmq.util.configuration.ErrorExchangeConfig;
import seek.util.mq.rabbitmq.util.configuration.MessageReturnCallBackConfig;
import seek.util.mq.rabbitmq.util.configuration.PropertiesConfig;
import seek.util.mq.rabbitmq.util.util.CorrleationUtil;
import seek.util.mq.rabbitmq.util.util.MQUtil;
import seek.util.mq.rabbitmq.util.util.QueueUtil;
import seek.util.mq.rabbitmq.util.util.RabbitDeclarationProvider;
import seek.util.mq.rabbitmq.util.util.RegisterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * rabbitmq-util 自动配置入口。
 * <p>
 * 引入本依赖即拆箱即用，全部基于 Spring 原生机制（@Import / @Bean / 依赖注入），
 * 不介入 Spring 的生命周期执行：
 * <ul>
 *     <li>@Import 的配置类由 ConfigurationClassPostProcessor 原生解析，@Bean 方法正常生效</li>
 *     <li>{@link PropertiesConfig} 通过 @EnableConfigurationProperties 自动绑定 yaml 配置</li>
 *     <li>业务方只需实现 {@link RabbitDeclarationProvider} 并注册为普通 bean，
 *     对应的交换机、队列与绑定关系即自动创建并绑定</li>
 * </ul>
 */
@AutoConfiguration
@EnableConfigurationProperties(PropertiesConfig.class)
@Import({ConverterConfig.class, ErrorExchangeConfig.class, MessageReturnCallBackConfig.class})
public class AutoConfig {

    @Bean
    public CorrleationUtil corrleationUtil() {
        return new CorrleationUtil();
    }

    @Bean
    public MQUtil mqUtil(RabbitTemplate rabbitTemplate, CorrleationUtil corrleationUtil) {
        return new MQUtil(rabbitTemplate, corrleationUtil);
    }

    @Bean
    public QueueUtil queueUtil() {
        return new QueueUtil();
    }

    @Bean
    public RegisterUtil registerUtil(QueueUtil queueUtil) {
        return new RegisterUtil(queueUtil);
    }

    /**
     * 收集容器中所有 {@link RabbitDeclarationProvider} 的声明数据，打包为单个 Declarables bean，
     * RabbitAdmin 启动时自动创建交换机、quorum 队列并完成绑定。
     * 无任何提供者时注册空声明，不影响启动。
     */
    @Bean
    public Declarables declarables(ObjectProvider<RabbitDeclarationProvider> providers, RegisterUtil registerUtil) {
        List<ExchangeData> exchangeDatas = new ArrayList<>();
        List<BindingData> bindingDatas = new ArrayList<>();
        providers.orderedStream().forEach(provider -> {
            if (provider.exchangeDatas() != null) exchangeDatas.addAll(provider.exchangeDatas());
            if (provider.bindingDatas() != null) bindingDatas.addAll(provider.bindingDatas());
        });
        return registerUtil.buildDeclarables(exchangeDatas, bindingDatas);
    }
}
