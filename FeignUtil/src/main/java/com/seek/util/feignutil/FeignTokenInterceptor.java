package com.seek.util.feignutil;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.seek.util.configobject.Context.TokenIdContext;

@Configuration
public class FeignTokenInterceptor {
    //构造器注入
    private static final Logger logger = LoggerFactory.getLogger(FeignTokenInterceptor.class);

    @Value("${seek.util.web.config.interceptor.request-header-token-id-name}")
    private String requestHeaderTokenIdName;
    @Value("${seek.util.sentinel.config.authority.request-header.name}")
    private String sentinelAuthorityHeaderName;
    @Value("${seek.util.sentinel.config.authority.request-header.value}")
    private String sentinelAuthorityHeaderValue;

    @Bean
    public RequestInterceptor tokenHeaderInterceptor() {
        return (template) -> {
            //获取当前Http上下文，并检查
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null)return;
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            //从tokenId线程上下文中取出tokenId
            String tokenId = TokenIdContext.get();
            logger.info("tokenId:{} ,调用了feign",tokenId);
            if (tokenId != null && !tokenId.isBlank()) {
                //Feign发起远程调用时自动带上该Header
                template.header(requestHeaderTokenIdName, tokenId);
                //用于sentinel检测
                template.header(sentinelAuthorityHeaderName, sentinelAuthorityHeaderValue);
            }
        };
    }
}