package com.violetbeach.daengbu.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@PropertySource(value = "classpath:exception.properties", encoding = "UTF-8")
public class PropertiesConfiguration {
	
    private final Environment env;

    public String getConfigValue(String configKey) {
        return env.getProperty(configKey);
    }
    
}