package com.violetbeach.daengbu.controller.v1.api;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.violetbeach.daengbu.controller.v1.command.SignupFormCommand;
import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.dto.response.Response;
import com.violetbeach.daengbu.service.UserService;
import com.violetbeach.daengbu.util.ClientIpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api/v1/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/check-dup-email")
	public Response checkDupEmail(@RequestParam("email") String email) {
		log.info("중복체크, email: '{}'", email);
		return Response.ok().setPayload(userService.isDuplicateEmail(email));
	}
	
	@GetMapping("/check-dup-username")
	public Response checkDupUsername(@RequestParam("username") String username) {
		log.info("중복체크, username: '{}'", username);
		return Response.ok().setPayload(userService.isDuplicateUsername(username));
	}
	 
	@PostMapping
	private RedirectView regist(@Valid @ModelAttribute("signupFormCommand") SignupFormCommand signupFormCommand, 
			HttpServletRequest request, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			log.error("binding error");	
			return new RedirectView("/signup");
		} else {
			UserDto userDto = new UserDto()
					.setEmail(signupFormCommand.getEmail())
					.setPassword(signupFormCommand.getPassword())
					.setUsername(signupFormCommand.getUsername())
					.setTel(signupFormCommand.getTel())
					.setRegisterIp(ClientIpUtils.getClientIP(request));
			userService.regist(userDto);
			return new RedirectView("/");
		}
	}
	
}