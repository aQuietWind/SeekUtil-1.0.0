package seek.util.web.web.util.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seek.util.web.web.util.config.GlobalRequestExceptionHandler;
import seek.util.web.web.util.config.PropertiesConfig;
import seek.util.web.web.util.config.TokenInterceptor;
import seek.util.web.web.util.config.WebConfig;

@Configuration
public class AutoConfig {
    @Bean
    public GlobalRequestExceptionHandler globalRequestExceptionHandler() {
        return new GlobalRequestExceptionHandler();
    }
    @Bean
    public TokenInterceptor tokenInterceptor(PropertiesConfig propertiesConfig) {
        return new TokenInterceptor(propertiesConfig);
    }
    @Bean
    public WebConfig webConfig() {
        return new WebConfig();
    }
}
