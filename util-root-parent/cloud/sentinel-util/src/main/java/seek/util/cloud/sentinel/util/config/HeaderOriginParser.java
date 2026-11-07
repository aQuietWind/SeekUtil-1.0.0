package seek.util.cloud.sentinel.util.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;


//请求头名单判别，用于Service模块对于sentinel的请求过滤
public class HeaderOriginParser implements RequestOriginParser {
    private final PropertiesConfig propertiesConfig;
    @Autowired
    public HeaderOriginParser(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }
    @Override
    public String parseOrigin(HttpServletRequest request){
        //可改为nacos_config统一配置,注意，这个请求头名字不能用origin，不然feign请求时会被吞掉
        String origin = request.getHeader(propertiesConfig.getAuthority().getRequestHeaderName());
        if (origin == null|| origin.isEmpty()) return "blank";   //返回blank
        return origin;      //重新返回该请求头标识
    }
}
