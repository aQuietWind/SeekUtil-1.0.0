package seek.util.cloud.gateway.util.autoconfigure;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seek.util.cloud.gateway.util.util.FilterUtil;

@Configuration
public class AutoConfig {

    @Bean
    public FilterUtil filterUtil(FilterUtil filterUtil){
        return new FilterUtil();
    }

}