package seek.util.common.event.respond.system.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seek.util.common.event.respond.system.config.PropertiesConfig;
import seek.util.common.event.respond.system.support.CheckPool;
import seek.util.common.event.respond.system.support.DelayPool;
import seek.util.common.event.respond.system.support.ExecutePool;
import seek.util.common.event.respond.system.util.ValueFactory;

@Configuration
public class AutoConfig {
    @Bean
    public DelayPool delayPool(PropertiesConfig propertiesConfig) {
        return new DelayPool(propertiesConfig);
    }

    @Bean
    public ExecutePool executePool(DelayPool delayPool,PropertiesConfig propertiesConfig) {
        return new ExecutePool(delayPool,propertiesConfig);
    }

    @Bean
    public CheckPool checkPool(ExecutePool executePool, DelayPool delayPool,PropertiesConfig propertiesConfig) {
        return new CheckPool(executePool, delayPool,propertiesConfig);
    }

    @Bean
    public ValueFactory valueFactory(CheckPool checkPool) {
        return new ValueFactory(checkPool);
    }
}
