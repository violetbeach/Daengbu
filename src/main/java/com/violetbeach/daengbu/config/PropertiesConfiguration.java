package com.violetbeach.daengbu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySources({
	@PropertySource(value = "classpath:exception.properties", encoding = "UTF-8"),
	@PropertySource(value = "classpath:config.properties")
})
public class PropertiesConfiguration {
	
    @Autowired
    private Environment env;

    public String getConfigValue(String configKey) {
        return env.getProperty(configKey);
    }
    
}