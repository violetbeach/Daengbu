package com.violetbeach.daengbu.controller.v1.api;

import static com.violetbeach.daengbu.security.SecurityConstants.EXPIRATION_TIME;
import static com.violetbeach.daengbu.security.SecurityConstants.SECRET;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.violetbeach.daengbu.dto.model.oauth.KakaoProfile;
import com.violetbeach.daengbu.dto.model.oauth.OAuthToken;
import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.dto.response.Response;
import com.violetbeach.daengbu.security.CustomUserDetailsService;
import com.violetbeach.daengbu.service.UserService;
import com.violetbeach.daengbu.util.ClientIpUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("rawtypes")
@RestController
public class OAuthController {

	@Autowired
	UserService userService;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@GetMapping("/oauth2/outhorization/kakao")
	public void kakaoLogin(HttpServletResponse res) {
		res.addHeader("Location", "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=0c5b4ac382c9d6c310cb82f7062d253f&redirect_uri=http://localhost:8080/oauth2/code/kakao");
	}
	
	@GetMapping("/oauth2/code/kakao")
	public ModelAndView getToken(String code, HttpServletResponse res) {
		
		RestTemplate rt = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "0c5b4ac382c9d6c310cb82f7062d253f");
		params.add("client_secret", "8hNHwTycmaqCVCLL3o2bxLnC8mQsnLzl");
		params.add("redirect_uri", "http://localhost:8080/oauth2/code/kakao");
		params.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
		                new HttpEntity<>(params, headers);

		ResponseEntity<String> tokenResponse = rt.exchange(
			"https://kauth.kakao.com/oauth/token",
			HttpMethod.POST,
			kakaoTokenRequest,
			String.class
		);
		
		ObjectMapper ob = new ObjectMapper();
		OAuthToken oauthToken = null;
		
		try {
			oauthToken = ob.readValue(tokenResponse.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		rt = new RestTemplate();

		headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
				new HttpEntity<>(headers); 

		ResponseEntity<String> profileResponse = rt.exchange(
		                "https://kapi.kakao.com/v2/user/me",
		                HttpMethod.POST,
		                kakaoProfileRequest,
		                String.class
		        );
		
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = ob.readValue(profileResponse.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		        
		if(userService.isDuplicateEmail(kakaoProfile.getKakao_account().getEmail())==true) {
			
			Claims claims = Jwts.claims().setSubject(kakaoProfile.getKakao_account().getEmail());
			List<String> role = new ArrayList<>();
			role.add("USER");
	        claims.put("role", role);
	        String token = Jwts.builder()
	        		.setClaims(claims)
	        		.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
	        		.signWith(SignatureAlgorithm.HS512, SECRET)
	        		.compact();
	        Cookie cookie = new Cookie("token", token);
	        cookie.setPath("/");
	        cookie.setMaxAge(60 * 60 * 24 * 7);
	        cookie.setHttpOnly(true);
	        res.addCookie(cookie);
		} else {
			ModelAndView modelAndView = new ModelAndView("oauth_signup");
			modelAndView.addObject("email", kakaoProfile.getKakao_account().getEmail());
			return modelAndView;
		}
		return new ModelAndView("redirect:/");
	}
	
	@PostMapping("/oauth2/user")
	public Response OAuthRegist(@RequestBody UserDto userDto, HttpServletRequest req, HttpServletResponse res) {
		userDto.setPassword(UUID.randomUUID().toString());
		userDto.setRegisterIp(ClientIpUtils.getClientIP(req));
		userService.regist(userDto);
		
		Claims claims = Jwts.claims().setSubject(userDto.getEmail());
		List<String> role = new ArrayList<>();
		role.add("USER");
        claims.put("role", role);
        String token = Jwts.builder()
        		.setClaims(claims)
        		.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        		.signWith(SignatureAlgorithm.HS512, SECRET)
        		.compact();
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setHttpOnly(true);
        res.addCookie(cookie);
		return Response.ok();
	}
	
}