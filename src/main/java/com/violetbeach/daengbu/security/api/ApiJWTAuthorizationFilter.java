package com.violetbeach.daengbu.security.api;

import static com.violetbeach.daengbu.security.SecurityConstants.SECRET;
import static com.violetbeach.daengbu.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@SuppressWarnings("unchecked")
public class ApiJWTAuthorizationFilter extends BasicAuthenticationFilter {
	
    public ApiJWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String token = getToken(req);
        if (token == null) {
            chain.doFilter(req, res);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (token != null) {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String user = claims.getSubject();
			ArrayList<String> role = (ArrayList<String>) claims.get("role");
            ArrayList<GrantedAuthority> list = new ArrayList<>();
            if (role != null) {
                for (String a : role) {
                    GrantedAuthority g = new SimpleGrantedAuthority(a);
                    list.add(g);
                }
            }
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, list);
            }
            return null;
        }
        return null;
    }
    
    private String getToken(HttpServletRequest request){
		Cookie cookies[] = request.getCookies();
		if(cookies!=null) {
			for(Cookie c:cookies) {
				if(c.getName().equals("token")) {
					return c.getValue();
				}
			}		
		}
		return null;
	}
    
}