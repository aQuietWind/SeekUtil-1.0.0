package seek.util.mq.rabbitmq.util.util;

import seek.util.common.config.object.mq.rabbitmq.BindingData;
import seek.util.common.config.object.mq.rabbitmq.ExchangeData;

import java.util.List;

/**
 * RabbitMQ 声明数据提供者。
 * <p>
 * 业务方实现该接口并注册为 Spring bean（@Component 或 @Bean 均可），
 * starter 会自动收集所有提供者的数据，由 RabbitAdmin 创建交换机、quorum 队列并完成绑定，
 * 效果与逐个 @Bean 声明 Queue/Exchange/Binding 完全一致，拆箱即用。
 * <p>
 * 用法示例：
 * <pre>{@code
 * @Component
 * public class OrderMqProvider implements RabbitDeclarationProvider {
 *     @Override
 *     public List<ExchangeData> exchangeDatas() {
 *         return List.of(new ExchangeData("order.exchange", ExchangeData.Topic));
 *     }
 *
 *     @Override
 *     public List<BindingData> bindingDatas() {
 *         return List.of(new BindingData(
 *                 new ExchangeData("order.exchange", ExchangeData.Topic),
 *                 "order.queue", "order.#"));
 *     }
 * }
 * }</pre>
 */
public interface RabbitDeclarationProvider {

    /**
     * 需要创建的交换机
     *
     * @return 交换机数据列表，默认为空
     */
    default List<ExchangeData> exchangeDatas() {
        return List.of();
    }

    /**
     * 需要创建的队列及其与交换机的绑定关系
     *
     * @return 绑定数据列表，默认为空
     */
    default List<BindingData> bindingDatas() {
        return List.of();
    }
}
