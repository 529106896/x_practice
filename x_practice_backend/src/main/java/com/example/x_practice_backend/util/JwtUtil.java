package com.example.x_practice_backend.util;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

// Jwt工具类
@Component
public class JwtUtil {
    // 有效期
    private static final long JWT_EXPIRE = 30 * 60 * 1000L; // 半小时
    // 令牌秘钥
    private static final String JWT_KEY = "watashiwakamisatoayakanoinu";

    // 用来对String进行Base64加密
    private SecretKey encodeSecret(String key) {
        byte[] encode = Base64.getEncoder().encode(key.getBytes());
        SecretKeySpec aes = new SecretKeySpec(encode, 0, encode.length, "HmacSHA256");
        return aes;
    }

    // 用来对传入的对象进行加密并生成Jwt-Token
    public String createToken(Object data) {
        // 当前时间
        long currentTime = System.currentTimeMillis();
        // 过期时间
        long expireTime = currentTime + JWT_EXPIRE;
        // 构建Jwt
        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID() + "")
                // 把要加密的对象放到subject中
                .setSubject(JSON.toJSONString(data))
                .setIssuer("system")
                .setIssuedAt(new Date(currentTime))
                // HS256是一种加密算法
                // 第二个参数需要是经过base转码过后的字符串，直接丟一个字符串进去会有问题
                .signWith(SignatureAlgorithm.HS256, encodeSecret(JWT_KEY))
                .setExpiration(new Date(expireTime));
        return builder.compact();
    }

    // 用来对token进行解密
    public Claims parseToken(String token) {
        Claims body = Jwts.parser()
                .setSigningKey(encodeSecret(JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return body;
    }

    // 解密方法的重载，可直接拿到对应的对象
    public <T> T parseToken(String token, Class<T> clazz) {
        Claims body = Jwts.parser()
                .setSigningKey(encodeSecret(JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return JSON.parseObject(body.getSubject(), clazz);
    }
}
