package com.violetbeach.daengbu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:exception.properties", encoding = "UTF-8")
public class PropertiesConfiguration {
	
    @Autowired
    private Environment env;

    public String getConfigValue(String configKey) {
        return env.getProperty(configKey);
    }
    
}