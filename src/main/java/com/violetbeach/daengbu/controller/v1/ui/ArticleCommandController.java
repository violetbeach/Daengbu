package com.violetbeach.daengbu.controller.v1.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.violetbeach.daengbu.controller.v1.command.ArticleSearchCommand;
import com.violetbeach.daengbu.controller.v1.command.PostFormCommand;
import com.violetbeach.daengbu.dto.model.article.ArticleDto;
import com.violetbeach.daengbu.dto.model.article.ArticleImageDto;
import com.violetbeach.daengbu.dto.model.article.KindDto;
import com.violetbeach.daengbu.dto.model.article.WishlistDto;
import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.service.ArticleService;
import com.violetbeach.daengbu.service.UserService;
import com.violetbeach.daengbu.util.DateUtils;

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
	public ModelAndView initArticleList() throws ParseException{
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("search", true);
		modelAndView.addObject("board_name", "분양 게시판");
		ArticleDto articleDto = new ArticleDto();
		List<ArticleDto> articleList = articleService.getArticleList(articleDto);
		List<ArticleImageDto> articleImageList = articleService.getArticleImageList(articleDto);
		modelAndView.addObject("maxNum",articleList.size());
		if(articleList.size()<9) {
			articleList=articleList.subList(0, articleList.size());
		}
		else {
			articleList=articleList.subList(0, 9);
		}
		List<KindDto> kindListDto = articleService.getKinds();
		for(int i = 0; i<articleList.size(); i++) {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(articleList.get(i).getCreatedDatetime());
			articleList.get(i).setCreatedDatetime(DateUtils.timeBefore(date));
			articleList.get(i).setRepImg(articleImageList.get(i).getId());
			for(KindDto kindDto : kindListDto) {
				if(articleList.get(i).getKindId().equals(kindDto.getId())) {
					articleList.get(i).setKindValue(kindDto.getValue());
					break;
				}
			}
		}
		modelAndView.addObject("articleList", articleList);
		return modelAndView;
	}
	
	@PostMapping("/search")
	public String initArticleList(@RequestParam(value="page", required=false) String page,
			ArticleSearchCommand articleSearchCommand, Model model) throws ParseException{
		model.addAttribute("board_name", "분양 게시판");
		ArticleDto articleDto = new ArticleDto()
				.setLocation1(articleSearchCommand.getLocation1())
				.setKindId(articleSearchCommand.getKindId())
				.setGender(articleSearchCommand.getGender())
				.setAge(articleSearchCommand.getAge());
		List<ArticleDto> articleList = articleService.getArticleList(articleDto);
		List<ArticleImageDto> articleImageList = articleService.getArticleImageList(articleDto);
		model.addAttribute("maxNum",articleList.size());
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
		List<KindDto> kindListDto = articleService.getKinds();
		for(int i = 0; i<articleList.size(); i++) {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(articleList.get(i).getCreatedDatetime());
			articleList.get(i).setCreatedDatetime(DateUtils.timeBefore(date));
			articleList.get(i).setRepImg(articleImageList.get(i).getId());
			for(KindDto kindDto : kindListDto) {
				if(articleList.get(i).getKindId().equals(kindDto.getId())) {
					articleList.get(i).setKindValue(kindDto.getValue());
					break;
				}
			}
		}
		model.addAttribute("articleList", articleList);
		return "index :: #index_fragment";
	}
	
	@GetMapping("/article/{id}")
	public ModelAndView initArticleDetail(@PathVariable Long id){
		ModelAndView modelAndView = new ModelAndView("detail");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		articleService.articleViewCount(id);
		ArticleDto articleDto = articleService.getById(id);
		modelAndView.addObject("articleDto", articleDto);
		modelAndView.addObject("author_username", userService.getUsernameById(articleDto.getAuthorId()));
		modelAndView.addObject("kind", articleService.getKindById(articleDto.getKindId()));
		modelAndView.addObject("content", articleService.getContentByArticleId(id));
		modelAndView.addObject("article_image", articleService.getArticleImageByArticleId(id));
		modelAndView.addObject("author_id", articleDto.getAuthorId());
		
		if(auth.getName()!="anonymousUser") {
			UserDto userDto = userService.findByEmail(auth.getName());
			
			if(userDto.getId()==articleDto.getAuthorId()) {
				modelAndView.addObject("role", "author");
			}
			
			WishlistDto wishlistDto = new WishlistDto()
					.setUserId(userDto.getId())
					.setArticleId(id);
			if(articleService.getWishCountById(wishlistDto)==true) modelAndView.addObject("wish", true);
			else modelAndView.addObject("wish", false);
		} else modelAndView.addObject("wish", false);
		
		return modelAndView;
	}
	
	@GetMapping("/article/my")
	public ModelAndView initMyArticleList(@RequestParam(value="page", required=false) String page) throws ParseException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto=userService.findByEmail(auth.getName());
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("board_name", "내 게시판");
		ArticleDto articleDto = new ArticleDto()
				.setAuthorId(userDto.getId());
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
		List<KindDto> kindListDto = articleService.getKinds();
		for(int i = 0; i<articleList.size(); i++) {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(articleList.get(i).getCreatedDatetime());
			articleList.get(i).setCreatedDatetime(DateUtils.timeBefore(date));
			articleList.get(i).setRepImg(articleImageList.get(i).getId());
			for(KindDto kindDto : kindListDto) {
				if(articleList.get(i).getKindId().equals(kindDto.getId())) {
					articleList.get(i).setKindValue(kindDto.getValue());
					break;
				}
			}
		}
		modelAndView.addObject("articleList", articleList);
		return modelAndView;
	}
	
	@GetMapping("/article/wish")
	public ModelAndView initWishArticleList(@RequestParam(value="page", required=false) String page) throws ParseException{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto=userService.findByEmail(auth.getName());
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("board_name", "관심 게시판");
		List<ArticleDto> articleList = articleService.getWishArticleList(userDto.getId());
		List<ArticleImageDto> articleImageList = articleService.getWishArticleImageList(userDto.getId());
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
		List<KindDto> kindListDto = articleService.getKinds();
		for(int i = 0; i<articleList.size(); i++) {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(articleList.get(i).getCreatedDatetime());
			articleList.get(i).setCreatedDatetime(DateUtils.timeBefore(date));
			articleList.get(i).setRepImg(articleImageList.get(i).getId());
			for(KindDto kindDto : kindListDto) {
				if(articleList.get(i).getKindId().equals(kindDto.getId())) {
					articleList.get(i).setKindValue(kindDto.getValue());
					break;
				}
			}
		}
		modelAndView.addObject("articleList", articleList);
		return modelAndView;
	}
	
	@GetMapping("/article/{id}/modify")
	public ModelAndView initUpdateForm(@PathVariable Long id){
		ModelAndView modelAndView = new ModelAndView("article_write");
		ArticleDto articleDto = articleService.getById(id);
		PostFormCommand postFormCommand = new PostFormCommand()
				.setArticleId(id)
				.setAge(articleDto.getAge())
				.setGender(articleDto.getGender())
				.setLocation1(articleDto.getLocation1())
				.setLocation2(articleDto.getLocation2())
				.setKindId(articleDto.getKindId())
				.setTitle(articleDto.getTitle())
				.setText(articleService.getContentByArticleId(id));
			
		modelAndView.addObject("postFormCommand", postFormCommand);
		modelAndView.addObject("kind", articleService.getKindById(articleDto.getKindId()));
		modelAndView.addObject("articleImageList", articleService.getArticleImageByArticleId(id));
		
		return modelAndView;
	}
	
}
