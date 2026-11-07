package seek.util.web.web.util.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("seek.util.web.web.config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertiesConfig {
    private String requestHeaderTokenIdName="tokenId";
}
