package com.seek.util.jwtutil;

import com.seek.util.configobject.JWTData.JWTGlobalData;
import com.seek.util.configobject.JWTData.JWTRoleData;
import com.seek.util.configobject.UtilObject.Exception.BizException;
import com.seek.util.configobject.UtilObject.Exception.ErrorCodeEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class JWTUtil {
    public static final String IdName="tokenId";
    private final ConcurrentHashMap<String,SecretKey> secretKeys=new ConcurrentHashMap<>();
    //获取密钥
    private SecretKey getSecretKey(String secretKey){
        //根据字符串密钥来生成HS256形式的密钥
        SecretKey key=secretKeys.get(secretKey);
        if(key==null||key.getEncoded()==null) {
            key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            secretKeys.put(secretKey,key);
        }
        return key;
    }
    //生成令牌的方法
    public String obtainJwt(Object tokenId, JWTRoleData jwtRoleData){
        return Jwts.builder()                  //通过链式编程生成字符串令牌
                .claims(Map.of(IdName,tokenId))                    //设置Payload载荷数据
                .subject(jwtRoleData.getSubject())                  //设置主题，id，或者权限等等重要标识
                .issuedAt(new Date())               //设置签发时间
                .expiration(new Date(System.currentTimeMillis()+jwtRoleData.getTokenDurationMillis()))             //设置最终有效期
                .signWith(getSecretKey(jwtRoleData.getSecretKey()))                      //设置该令牌密钥，自动识别HS256算法
                .compact();                         //根据上述设置生成一个令牌字符串
    }

    //解析并获取令牌内容的方法
    public Long jwtCheck(String token,String secretKey){
            Jws<Claims> data = Jwts.parser()           //开启解析器建立
                    .verifyWith(getSecretKey(secretKey))                    //设置解析的密钥
                    .build()                            //根据上述建立一个解析器
                    .parseSignedClaims(token);          //通过解析器获取指定令牌的破解版
            return (Long) data.getPayload().get(IdName);       //返回载荷存储的userId
    }

    //解析并获取令牌内容的方法
    public Long jwtCheck(String token,JWTRoleData jwtRoleData){
        return jwtCheck(token,jwtRoleData.getSecretKey());
    }

    public TokenCheckResult jwtCheckByList(String token,String headerSeparator,HashMap<String,String> jwtHeaders){
        //分割token,并且赋值
        String[] body=token.split(headerSeparator,2);
        String headerSign=body[0];
        token=body[1];
        //查找对应的secretKey进行解析
        if (jwtHeaders.containsKey(headerSign))return new TokenCheckResult(token,jwtCheck(token,jwtHeaders.get(headerSign)));
        //啥也没查到就抛错
        throw new BizException(ErrorCodeEnum.UNAUTHORIZED);
    }

    public TokenCheckResult jwtCheckByList(String token, JWTGlobalData jwtGlobalData, HashMap<String,String> jwtHeaders){
        return jwtCheckByList(token,jwtGlobalData.getTokenHeaderSeparator(),jwtHeaders);
    }






















}
