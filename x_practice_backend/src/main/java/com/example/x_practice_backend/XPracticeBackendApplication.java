package com.example.x_practice_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@MapperScan("com.example.x_practice_backend.*.mapper")
public class XPracticeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(XPracticeBackendApplication.class, args);
	}

	// 用于密码加密
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
