package com.hrm.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 *  token生成工具类
 * </p>
 *
 * @author guozy
 * @create 2019/09/18
 */
@Setter
@Getter
@ConfigurationProperties("jwt.config")
public class JwtUtils {
    /**
     * 签名私钥
     */
    private String key;
    /**
     * 过期时间
     */
    private Long ttl;

    /**
     * 创建token
     * @param id 用户ID
     * @param name 用户名称
     * @param map  自定义属性
     * @return
     */
    public String createJwt(String id,String name,Map<String,Object> map){
        //设置失效时间
        //当前时间
        long now = System.currentTimeMillis();
        //失效时间
        long exp=now+ttl;
        JwtBuilder builder = Jwts.builder().setId(id)
                .setSubject(name)
                .setIssuedAt(new Date())//签发时间
                .signWith(SignatureAlgorithm.HS256, key)//加密类型+私钥
                .setClaims(map)//自定义claims
                .setExpiration(new Date(exp));//失效时间
        //创建token
        return builder.compact();
    }

    /**
     * 解析token字符串，获取claims
     * @param token
     * @return
     */
    public Claims parseJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }
}
