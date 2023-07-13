package com.example.x_practice_backend;

import com.example.x_practice_backend.sys.entity.User;
import com.example.x_practice_backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testCreateJwt() {
        User user = new User();
        user.setUsername("keqing");
        user.setPhone("18603899637");
        String token = jwtUtil.createToken(user);
        System.out.println(token);
    }

    @Test
    public void testParseJwt() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlYWNmNDhiYi1iN2VjLTRmYzUtYmQ2OS1mN2NjM2UyOGUzMDEiLCJzdWIiOiJ7XCJwaG9uZVwiOlwiMTg2MDM4OTk2MzdcIixcInVzZXJuYW1lXCI6XCJrZXFpbmdcIn0iLCJpc3MiOiJzeXN0ZW0iLCJpYXQiOjE2ODkyMjM0NjYsImV4cCI6MTY4OTIyNTI2Nn0.6lae11mL0sMhRzH8QSrC1Y6A6WxVZH6To4z-2TJPtAY";
        Claims claims = jwtUtil.parseToken(token);
        System.out.println(claims);
    }

    @Test
    public void testParseJwtToObject() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmYTEwOTIxNS00OGM0LTQ5MWQtYWE5Zi04ZGI3NzFhMzFlM2YiLCJzdWIiOiJ7XCJhdmF0YXJcIjpcImh0dHBzOi8vaW1nMS5iYWlkdS5jb20vaXQvdT0xODI5NTk5MTM0LDg1NjA3ODQyNiZmbT0yNTMmZm10PWF1dG8mYXBwPTEzOCZmPUpQRUc_dz01MDAmaD00OThcIixcImNyZWF0ZVRpbWVcIjpcIjIwMjMtMDctMTIgMTc6Mjc6NDRcIixcImRlbGV0ZWRcIjowLFwiZW1haWxcIjpcImtlcWluZ0BsaXl1ZS5jb21cIixcImlkXCI6MSxcInBob25lXCI6XCIxODYwMzg5OTYzNFwiLFwic3RhdHVzXCI6MSxcInVwZGF0ZVRpbWVcIjpcIjIwMjMtMDctMTIgMTc6NTk6MTRcIixcInVzZXJuYW1lXCI6XCJrZXFpbmdcIn0iLCJpc3MiOiJzeXN0ZW0iLCJpYXQiOjE2ODkyMjYyNDksImV4cCI6MTY4OTIyODA0OX0.iDO3jiOoQJsL7bjKu1LXUHior_5G8ahIL_5olQR65ok";
        User user = jwtUtil.parseToken(token, User.class);
        System.out.println(user);
    }
}
