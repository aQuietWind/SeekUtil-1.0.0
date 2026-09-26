package com.seek.util.gatewayutil;

import com.google.gson.Gson;
import com.seek.util.configobject.UtilObject.DTO.Result;
import com.seek.util.configobject.UtilObject.Exception.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class FilterUtil {
    //字节码化的Result
    private final static byte[] errorBytes = new Gson().toJson(Result.error(ErrorCodeEnum.UNAUTHORIZED)).getBytes();
    //拒绝放行
    public Mono<Void> reject(ServerWebExchange exchange) {
        log.warn("非法请求被Filter拦截");
        ServerHttpResponse response=exchange.getResponse();
        response.setStatusCode(ErrorCodeEnum.UNAUTHORIZED.getHttpStatus());      //设置状态码
        try {
            //使返回失败结果
            return response.writeWith(Mono.just(response.bufferFactory().wrap(errorBytes)));
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return response.setComplete();
        }
    }

    //追加请求头
    public ServerWebExchange addAndOverwriteHeader(ServerWebExchange exchange,
            String headerName,String headerValue){
        // 构造新请求，追加解析后的用户信息到请求头
        ServerHttpRequest newReq = exchange.getRequest().mutate()
                .headers(headers -> {
                    // 清空客户端伪造的同名header
                    headers.remove(headerName);
                    // 新增，此时列表只有一条
                    headers.add(headerName, headerValue);
                })
                .build();
        //放入id
        return exchange.mutate().request(newReq).build();
    }
}
