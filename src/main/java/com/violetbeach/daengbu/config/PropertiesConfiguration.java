package com.violetbeach.daengbu.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@PropertySources({
	@PropertySource(value = "classpath:exception.properties", encoding = "UTF-8"),
	@PropertySource(value = "classpath:config.properties")
})
public class PropertiesConfiguration {
	
    private final Environment env;

    public String getConfigValue(String configKey) {
        return env.getProperty(configKey);
    }
    
}