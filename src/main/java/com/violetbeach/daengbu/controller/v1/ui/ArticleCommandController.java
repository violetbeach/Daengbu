package com.violetbeach.daengbu.controller.v1.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.violetbeach.daengbu.controller.v1.command.PostFormCommand;
import com.violetbeach.daengbu.service.ArticleService;
import com.violetbeach.daengbu.service.UserService;

@Controller
public class ArticleCommandController {
	
	@Autowired
	ArticleService articleService = new ArticleService();
	
	@Autowired
	UserService userService = new UserService();
	
	@GetMapping("/article/new")
	public ModelAndView initPostForm() {
		ModelAndView modelAndView = new ModelAndView("article_write");
		modelAndView.addObject("postFormCommand", new PostFormCommand());
		return modelAndView;
	}
	
}
