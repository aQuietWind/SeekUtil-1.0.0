package seek.util.web.web.util.config;

import org.springframework.beans.factory.annotation.Autowired;
import seek.util.common.config.object.common.context.TokenIdContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class TokenInterceptor implements HandlerInterceptor {

    private final PropertiesConfig propertiesConfig;
    @Autowired
    public TokenInterceptor(PropertiesConfig propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Handle)throws Exception{
        // 获取TokenId
        String tokenId  = request.getHeader(propertiesConfig.getRequestHeaderTokenIdName());
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
