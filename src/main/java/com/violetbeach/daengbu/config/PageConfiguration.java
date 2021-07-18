package com.violetbeach.daengbu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PageConfiguration implements WebMvcConfigurer{
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/signup").setViewName("signup");
		registry.addViewController("/oauth/signup").setViewName("oauth_signup");
		registry.addViewController("/naming").setViewName("naming");
	}
	
}