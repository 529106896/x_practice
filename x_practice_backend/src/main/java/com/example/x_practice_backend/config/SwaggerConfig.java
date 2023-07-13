package com.example.x_practice_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

// Swagger配置类
@Configuration
@EnableOpenApi
@EnableWebMvc
/**
 * 使用http://localhost:9999/swagger-ui/index.html来访问Swagger文档
 * @Api: 用于修饰Controller类，生成Controller相关文档信息
 * @ApiOperation: 用于修饰Controller类中的方法，生成接口方法相关文档信息
 * @ApiParam: 用于修饰接口中的参数，生成接口参数相关文档信息
 * @ApiModelProperty: 用于修饰实体类的属性，当实体类是请求参数或返回结果时，直接生成文档信息
 */
public class SwaggerConfig {


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.x_practice_backend"))
                .paths(PathSelectors.any())
                .build()
                // 添加下面两行用于Swagger中的Jwt认证
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 标题
                .title("原神管理系统接口文档")
                // 描述
                .description("原神？启动！我是神里绫华的狗！")
                // 版本号
                .version("1.0")
                // 联系人
                .contact(new Contact("袁佳哲", "https://github.com/529106896", "529106896@qq.com"))
                .build();
    }

    // 下面用于让Swagger能够测试需要Jwt的接口

    private SecurityScheme securityScheme() {
        return new ApiKey("X-Token", "X-Token", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("^(?!auth).*$"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference("X-Token", authorizationScopes)
        );
    }
}
