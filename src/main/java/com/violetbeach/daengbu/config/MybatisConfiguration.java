package com.violetbeach.daengbu.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
@MapperScan(basePackages = "com.violetbeach.daengbu.repository")
public class MybatisConfiguration {
	
	private final DataSource dataSource;
	
	@Bean
	public SqlSessionFactory sqlSessionFactory(ApplicationContext applicationContext) throws Exception{
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/sql/*.xml"));
		org.springframework.core.io.Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml");
	    factoryBean.setConfigLocation(myBatisConfig);
		SqlSessionFactory factory = factoryBean.getObject();
		return factory;
	}
	
	@Bean
	public SqlSessionTemplate sqlEssionTemplate(@Autowired SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
