package seek.util.db.redis.util.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("spring.data.redis")
public class PropertiesConfig {
    private String host;
    private String port;
    private String password;
}
