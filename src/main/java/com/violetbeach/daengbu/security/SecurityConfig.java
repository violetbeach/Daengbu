package com.violetbeach.daengbu.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.violetbeach.daengbu.security.api.ApiJWTAuthenticationFilter;
import com.violetbeach.daengbu.security.api.ApiJWTAuthorizationFilter;
import com.violetbeach.daengbu.security.api.RefererFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
    	web.ignoring().antMatchers(
    			"/resources/**", "/static/**", "/styles/**", "/js/**", "/img/**",
                "/resources/static/**", "/js/**", "/img/**", "/font/**",
                "/vendor/**", "/svg/**", "/model/**", "/oauth2/**",
                "/*");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
        	.httpBasic().disable()
        	.csrf().disable()
        	.authorizeRequests() 
        	.antMatchers("/api/v1/article/wish").authenticated()
        	.antMatchers("/api/v1/article").authenticated()
        	.antMatchers("/api/v1/chat/room").authenticated()
        	.anyRequest().permitAll()
        	.and()
        	.exceptionHandling()
        	.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
        	.and()
        	.addFilter(new ApiJWTAuthenticationFilter(authenticationManager()))
        	.addFilter(new ApiJWTAuthorizationFilter(authenticationManager()))
        	.addFilterBefore(new RefererFilter(), ApiJWTAuthorizationFilter.class)
        	.sessionManagement()
        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    }

}