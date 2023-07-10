package com.example.x_practice_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// 跨域配置类
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许谁访问，这里配置为允许前端访问
        configuration.addAllowedOrigin("http://localhost:8888");
        // 设置允许Cookie
        configuration.setAllowCredentials(true);
        // 允许哪些请求方法
        configuration.addAllowedMethod("*");
        // 允许哪些Header
        configuration.addAllowedHeader("*");
        // 添加映射路径，即哪些请求需要被过滤器拦截
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
