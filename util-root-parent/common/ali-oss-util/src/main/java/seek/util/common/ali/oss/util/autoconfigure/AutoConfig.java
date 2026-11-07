package seek.util.common.ali.oss.util.autoconfigure;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seek.util.common.ali.oss.util.util.OssUtil;

@Configuration
public class AutoConfig {

    @Bean
    public OssUtil ossUtil(){
        return new OssUtil();
    }

}