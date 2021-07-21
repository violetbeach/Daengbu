package com.violetbeach.daengbu.controller.v1.api;

import java.util.Random;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.violetbeach.daengbu.controller.v1.command.ChangePasswordCommand;
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
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@GetMapping("/me")
	public Response findByToken(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.getName()!="anonymousUser") {
			UserDto userDto=userService.findByEmail(auth.getName());
			return Response.ok().setPayload(userDto);
		} else return Response.badRequest().setPayload(new UserDto());
	}
	
	@GetMapping("/logout")
	public void deleteCookie(HttpServletResponse res) {
		Cookie cookie = new Cookie("token", null);
		cookie.setMaxAge(0);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		res.addCookie(cookie);
	}
	
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
	private ModelAndView regist(@Valid @ModelAttribute("signupFormCommand") SignupFormCommand signupFormCommand, 
			HttpServletRequest request, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			log.error("binding error");
			return new ModelAndView("signup");
		} else {
			try {	UserDto userDto = new UserDto()
					.setEmail(signupFormCommand.getEmail())
					.setPassword(signupFormCommand.getPassword())
					.setUsername(signupFormCommand.getUsername())
					.setTel(signupFormCommand.getTel())
					.setRegisterIp(ClientIpUtils.getClientIP(request));
					userService.regist(userDto);
			} catch (Exception exception) {
				return new ModelAndView("signup");
			}
		}
		ModelAndView modelAndView = new ModelAndView("signup_result");
		modelAndView.addObject("username", signupFormCommand.getUsername());
		return modelAndView;
	}
	
	@PostMapping("/email-auth")
	public Response sendMail(String mail) throws Exception {
		MimeMessage message = javaMailSender.createMimeMessage();
		message.addRecipients(RecipientType.TO, mail);
		String key = "";
		Random random = new Random();
		for(int i = 0; i < 3; i++) {
			key += (char)(random.nextInt(25) + 65);
		}
		key += random.nextInt(9999) + 1000;
		message.setFrom(new InternetAddress("reply@daengbu.co.kr", "댕부"));
		message.setSubject("DaengBu 인증번호가 도착했습니다.");
		message.setText("인증번호 : " + key);
		userService.addMailAuth(mail, key);
		javaMailSender.send(message);
		return Response.ok();
	}
	
	@GetMapping("/email-auth")
	public Response emailAuth(String mail, String auth) throws Exception {
		if(userService.getMailAuthByEmail(mail).equals(auth)) {
			return Response.ok();
		} else return Response.notFound().setPayload("인증번호가 정확하지 않습니다.");
	}
	
	@PutMapping("/change-password")
	public Response changePassword(@RequestBody ChangePasswordCommand changePasswordCommand) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto=userService.findByEmail(auth.getName());
		if(userService.isMatch(changePasswordCommand.getOldPassword(), userDto.getPassword())) {
			userService.changePassword(userDto, changePasswordCommand.getNewPassword());
		} else Response.notFound().setPayload(changePasswordCommand.getOldPassword());
		return Response.ok();
	}
	
}