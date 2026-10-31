package seek.util.common.config.object.RabbitMQData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeData {
    private String exchangeName;
    private String type;
}
