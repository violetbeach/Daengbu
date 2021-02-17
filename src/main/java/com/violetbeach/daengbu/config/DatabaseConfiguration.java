package com.violetbeach.daengbu.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {
	
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}
	
}