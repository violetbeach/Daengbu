package com.violetbeach.daengbu.controller.v1.ui;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.violetbeach.daengbu.controller.v1.command.SignupFormCommand;
import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserCommandController {
	
	private final UserService userService;

	@GetMapping("/signup")
	public ModelAndView initSignupForm() {
		ModelAndView modelAndView = new ModelAndView("signup");
		modelAndView.addObject("signupFormCommand", new SignupFormCommand());
		return modelAndView;
	}
	
	@PostMapping("/id-inquiry/result")
	public String initIdInquiryForm(String tel, Model model) {
		String id_result = userService.getEmailByTel(tel);
		if (id_result == null) id_result = "not_found";
		model.addAttribute("id_result", id_result);
		return "id_inquiry :: #fragment";
	}
	
	@PostMapping("/pw-inquiry/result")
	public String initPwInquiryForm(String mail, String auth, Model model) {
		if(userService.getMailAuthByEmail(mail).equals(auth)) {
			String key = "";
			Random random = new Random();
			for(int i = 0; i < 6; i++) {
				key += (char)(random.nextInt(25) + 65);
			}
			key += random.nextInt(9999) + 1000;
			UserDto userDto = userService.findByEmail(mail);
			userService.changePassword(userDto, key);
			model.addAttribute("pw_result", key);
		} else {
			
		}
		return "pw_inquiry :: #fragment";
	}
	
}
