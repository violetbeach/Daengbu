package com.violetbeach.daengbu.controller.v1.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.violetbeach.daengbu.controller.v1.command.PostFormCommand;
import com.violetbeach.daengbu.dto.model.article.ArticleDto;
import com.violetbeach.daengbu.dto.model.article.ArticleImageDto;
import com.violetbeach.daengbu.dto.model.article.ContentDto;
import com.violetbeach.daengbu.dto.model.article.KindDto;
import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.service.ArticleService;
import com.violetbeach.daengbu.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping("/api/v1/article")
public class ArticleController {
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ArticleService articleService;
	
	@GetMapping("/kind")
	public List<KindDto> getKinds() {
		return articleService.getKinds();
	}
	
	@PostMapping
	private String post(@ModelAttribute("postFormCommand") PostFormCommand postFormCommand, 
			@RequestPart List<MultipartFile> images, HttpServletResponse response) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDto userDto=userService.findByEmail(auth.getName());
			ArticleDto articleDto = new ArticleDto()
					.setAuthorId(userDto.getId())
					.setKindId(postFormCommand.getKindId())
					.setLocation1(postFormCommand.getLocation1())
					.setLocation2(postFormCommand.getLocation2())
					.setAge(postFormCommand.getAge())
					.setGender(postFormCommand.getGender())
					.setTitle(postFormCommand.getTitle());
			
			ContentDto contentDto = new ContentDto()
					.setAuthorId(articleDto.getAuthorId())
					.setText(postFormCommand.getText());
			
			List<ArticleImageDto> listArticleImageDto = new ArrayList<>();
			String fileUrl = "D:\\eclipse-workspace\\daengbu\\src\\main\\resources\\static\\img\\post\\";
			
			for(int i=0;i<images.size();i++) {
				listArticleImageDto.add(new ArticleImageDto());
			}
			articleService.post(articleDto, contentDto, listArticleImageDto);
			
			int count=0;	
			for(MultipartFile file : images) {
				File image = new File(fileUrl+listArticleImageDto.get(count).getId()+".png");
				file.transferTo(image);
				count++;
			}
			
			return "/article/"+articleDto.getId();
			
			} catch (Exception exception) {
			}
		return "/";
	}
	
}