package com.violetbeach.daengbu.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class AppConfiguration {
	
	@Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Bean
    public Docket swaggerUserApi() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.groupName("Daengbu")
        		.select()
                .apis(RequestHandlerSelectors.basePackage("com.violetbeach.daengbu.controller.v1.api"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .securitySchemes(Arrays.asList(apiKey()));
    }
    
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Daengbu - REST APIs")
                .description("Daengbu application.").termsOfServiceUrl("")
                .contact(new Contact("Daengbu github", "https://github.com/VioletBeach/Daengbu", "ti6419@gmail.com"))
                .version("1.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("apiKey", "Authorization", "header");
    }
    
}