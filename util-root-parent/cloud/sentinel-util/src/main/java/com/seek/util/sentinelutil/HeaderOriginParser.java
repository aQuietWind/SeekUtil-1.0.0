package com.seek.util.sentinelutil;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

// 仅普通Servlet微服务生效，WebFlux网关不会实例化这个类
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//请求头名单判别，用于Service模块对于sentinel的请求过滤
@Component
public class HeaderOriginParser implements RequestOriginParser {
    @Value("${seek.util.sentinel.config.authority.request-header.name}")
    private String sentinelAuthorityHeaderName;
    @Override
    public String parseOrigin(HttpServletRequest request){
        //可改为nacos_config统一配置，但我懒。注意，这个请求头名字不能用origin，不然feign请求时会被吞掉
        String origin = request.getHeader(sentinelAuthorityHeaderName);
        if (origin == null|| origin.isEmpty()) return "blank";   //返回blank
        return origin;      //重新返回该请求头标识
    }
}
