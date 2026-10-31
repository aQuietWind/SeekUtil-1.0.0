package com.seek.util.elasticsearchutil.AutoConfig;

import com.seek.util.elasticsearchutil.EsSearchUtil;
import com.seek.util.elasticsearchutil.EsUpdateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;

@Configuration
public class UtilAutoConfig {
    @Bean
    @Lazy
    public EsSearchUtil esSearchUtil(ElasticsearchTemplate elasticsearchTemplate) {
        return new EsSearchUtil(elasticsearchTemplate);
    }
    @Bean
    @Lazy
    public EsUpdateUtil esUpdateUtil(ElasticsearchTemplate elasticsearchTemplate) {
        return new EsUpdateUtil(elasticsearchTemplate);
    }
}
