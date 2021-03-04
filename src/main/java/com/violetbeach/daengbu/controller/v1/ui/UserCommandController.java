package com.violetbeach.daengbu.controller.v1.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.violetbeach.daengbu.controller.v1.command.SignupFormCommand;

@Controller
public class UserCommandController {

	@GetMapping("/signup")
	public ModelAndView initSignupForm() {
		ModelAndView modelAndView = new ModelAndView("signup");
		modelAndView.addObject("signupFormCommand", new SignupFormCommand());
		return modelAndView;
	}
	
}
