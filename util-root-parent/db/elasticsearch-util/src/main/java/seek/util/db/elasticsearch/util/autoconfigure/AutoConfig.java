package seek.util.db.elasticsearch.util.autoconfigure;

import seek.util.db.elasticsearch.util.config.EsConfig;
import seek.util.db.elasticsearch.util.util.EsSearchUtil;
import seek.util.db.elasticsearch.util.util.EsUpdateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;

@Configuration
public class AutoConfig {
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
    @Bean
    public EsConfig esConfig() {
        return new EsConfig();
    }
}
