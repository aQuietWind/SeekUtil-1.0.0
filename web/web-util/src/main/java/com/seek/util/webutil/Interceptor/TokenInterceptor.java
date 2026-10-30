package com.seek.util.webutil.Interceptor;

import com.seek.util.configobject.Context.TokenIdContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    @Value("${seek.util.web.config.interceptor.request-header-token-id-name}")
    private String requestHeaderTokenIdName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Handle)throws Exception{
        // 获取TokenId
        String tokenId  = request.getHeader(requestHeaderTokenIdName);
        log.info("tokenId:{} ,进入该模块", tokenId);
        //放入context上下文
        if (tokenId!=null) TokenIdContext.set(tokenId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handle, Exception ex)throws Exception{
        TokenIdContext.remove();
    }
}
