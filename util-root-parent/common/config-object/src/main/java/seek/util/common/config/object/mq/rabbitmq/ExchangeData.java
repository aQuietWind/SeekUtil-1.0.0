package seek.util.common.config.object.mq.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeData {
    public static final String Fanout="fanout";
    public static final String Direct="direct";
    public static final String Topic="topic";
    private String exchangeName;
    private String type;
}
