package com.seek.util.elasticsearchutil.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.data.mapping.model.SnakeCaseFieldNamingStrategy;

@Configuration
public class EsConfig {
    @Bean
    public SimpleElasticsearchMappingContext elasticsearchMappingContext() {
        SimpleElasticsearchMappingContext mappingContext = new SimpleElasticsearchMappingContext();
        // 全局开启驼峰自动转下划线（官方标准API）
        mappingContext.setFieldNamingStrategy(new SnakeCaseFieldNamingStrategy());
        return mappingContext;
    }
}
