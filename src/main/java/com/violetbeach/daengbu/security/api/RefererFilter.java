package com.violetbeach.daengbu.security.api;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter
public class RefererFilter implements Filter {
    private static final List<String> acceptableDomains = Arrays.asList("google.com", "localhost", "daengbu.tech");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
	
        String refererHeader = request.getHeader("referer");
        if (refererHeader == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        URL refererURL = new URL(refererHeader);
        String[] hostParts = refererURL.getHost().split(Pattern.quote("."));

        if (hostParts.length == 1) {
            if (!acceptableDomains.contains(hostParts[0])) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        } else if (hostParts.length >= 2) {
            if (!acceptableDomains.contains(hostParts[hostParts.length - 2] + "." + hostParts[hostParts.length - 1])) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
    
}