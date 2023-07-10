package com.example.x_practice_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.x_practice_backend.*.mapper")
public class XPracticeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(XPracticeBackendApplication.class, args);
	}

}
