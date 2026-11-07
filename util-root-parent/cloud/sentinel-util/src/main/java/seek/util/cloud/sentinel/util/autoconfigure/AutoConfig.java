package seek.util.cloud.sentinel.util.autoconfigure;


import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seek.util.cloud.sentinel.util.config.HeaderOriginParser;
import seek.util.cloud.sentinel.util.config.PropertiesConfig;
import seek.util.cloud.sentinel.util.config.SentinelBlockHandler;
import seek.util.cloud.sentinel.util.config.SentinelGlobalApiConfig;

@Configuration
public class AutoConfig {

    @Bean
    public PropertiesConfig propertiesConfig(){
        return new PropertiesConfig();
    }
    @Bean
    public RequestOriginParser headerOriginParser(PropertiesConfig propertiesConfig){
        return new HeaderOriginParser(propertiesConfig);
    }

    @Bean
    public BlockExceptionHandler blockExceptionHandler(){
        return new SentinelBlockHandler();
    }

    @Bean
    public SentinelGlobalApiConfig sentinelGlobalApiConfig(){
        return new SentinelGlobalApiConfig();
    }

}