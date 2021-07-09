package com.violetbeach.daengbu.controller.v1.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.violetbeach.daengbu.controller.v1.command.ArticleSearchCommand;
import com.violetbeach.daengbu.controller.v1.command.PostFormCommand;
import com.violetbeach.daengbu.dto.model.article.ArticleDto;
import com.violetbeach.daengbu.dto.model.article.ArticleImageDto;
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
	
	@GetMapping
	public ModelAndView initArticleList(@RequestParam(value="page", required=false) String page,
			ArticleSearchCommand articleSearchCommand){
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("search", true);
		modelAndView.addObject("board_name", "분양 게시판");
		ArticleDto articleDto = new ArticleDto()
				.setLocation1(articleSearchCommand.getLocation1())
				.setKindId(articleSearchCommand.getKindId())
				.setGender(articleSearchCommand.getGender())
				.setAge(articleSearchCommand.getAge());
		List<ArticleDto> articleList = articleService.getArticleList(articleDto);
		List<ArticleImageDto> articleImageList = articleService.getArticleImageList(articleDto);
		modelAndView.addObject("maxNum",articleList.size());
		try {
			int p = Integer.parseInt(page);
			if(articleList.size()<p*9) {
				articleList=articleList.subList((p*9)-9, (p*9)-9+articleList.size()%9);
			}
			else {
				articleList=articleList.subList((p*9)-9, p*9);
			}
		} catch(NumberFormatException e) {
			if(articleList.size()<9) {
				articleList=articleList.subList(0, articleList.size());
			}
			else {
				articleList=articleList.subList(0, 9);
			}
		}
		for(int i = 0; i<articleList.size(); i++) {
			articleList.get(i).setRepImg(articleImageList.get(i).getId());
		}
		modelAndView.addObject("articleList", articleList);
		return modelAndView;
	}
	
}
