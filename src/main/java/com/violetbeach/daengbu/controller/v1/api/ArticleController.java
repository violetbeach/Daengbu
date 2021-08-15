package com.violetbeach.daengbu.controller.v1.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.violetbeach.daengbu.controller.v1.command.PostFormCommand;
import com.violetbeach.daengbu.dto.model.article.ArticleDto;
import com.violetbeach.daengbu.dto.model.article.ArticleImageDto;
import com.violetbeach.daengbu.dto.model.article.ContentDto;
import com.violetbeach.daengbu.dto.model.article.KindDto;
import com.violetbeach.daengbu.dto.model.article.WishlistDto;
import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.dto.response.Response;
import com.violetbeach.daengbu.service.ArticleService;
import com.violetbeach.daengbu.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@SuppressWarnings("rawtypes")
@RequestMapping("/api/v1/article")
public class ArticleController {
	
	private final UserService userService;
	
	private final ArticleService articleService;
	
	String sep = File.separator;
	String imgPath = new File("").getAbsolutePath() + sep + "src" + sep + "main" + sep + "resources" + sep + "static" + sep + "img" + sep + "post" + sep;

	@ApiOperation(value = "견종 리스트 조회", notes = "서버에 등록된 견종 목록을 제공하는 API입니다.")
	@GetMapping("/kind")
	public List<KindDto> getKinds() {
		return articleService.getKinds();
	}
	
	@ApiOperation(value = "게시글 생성", notes = "입력 폼으로 게시글을 생성합니다.")
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
			
			for(int i=0;i<images.size();i++) {
				listArticleImageDto.add(new ArticleImageDto());
			}
			articleService.post(articleDto, contentDto, listArticleImageDto);
			
			int count=0;	
			for(MultipartFile file : images) {
				File image = new File(imgPath+listArticleImageDto.get(count).getId()+".png");
				file.transferTo(image);
				count++;
			}
			
			return "/article/"+articleDto.getId();
			
			} catch (Exception exception) {
			}
		return "/";
	}
	
	@ApiOperation(value = "게시글 삭제", notes = "게시글 id로 게시글을 삭제합니다.")
	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Response delArticle(Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto = userService.findByEmail(auth.getName());
		ArticleDto articleDto = articleService.getById(id);
		if(userDto.getId()==articleDto.getAuthorId()) {
			articleService.delArticle(id);
		}
		return Response.unauthorized();
	}
	
	@ApiOperation(value = "찜 생성", notes = "게시글 id로 해당 게시글에 찜을 생성합니다.")
	@PostMapping("/wish")
	public void addWishlist(Long articleId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto = userService.findByEmail(auth.getName());
		WishlistDto wishlistDto = new WishlistDto()
				.setUserId(userDto.getId())
				.setArticleId(articleId);
		articleService.addWishlist(wishlistDto);
	}
	
	@ApiOperation(value = "찜 삭제", notes = "게시글 id로 해당 게시글에 찜을 삭제합니다.")
	@DeleteMapping("/wish")
	public void delWishlist(Long articleId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDto userDto = userService.findByEmail(auth.getName());
		WishlistDto wishlistDto = new WishlistDto()
				.setUserId(userDto.getId())
				.setArticleId(articleId);
		articleService.delWishlist(wishlistDto);
	}
	
	@ApiOperation(value = "게시글 수정", notes = "입력 폼으로 게시글을 수정합니다.")
	@PutMapping
	private Response update(@ModelAttribute("postFormCommand") PostFormCommand postFormCommand, 
			@RequestPart List<MultipartFile> images, HttpServletResponse response) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDto userDto=userService.findByEmail(auth.getName());
			ArticleDto articleDto = new ArticleDto()
					.setId(postFormCommand.getArticleId())
					.setAuthorId(userDto.getId())
					.setKindId(postFormCommand.getKindId())
					.setLocation1(postFormCommand.getLocation1())
					.setLocation2(postFormCommand.getLocation2())
					.setAge(postFormCommand.getAge())
					.setGender(postFormCommand.getGender())
					.setTitle(postFormCommand.getTitle());
			
			ContentDto contentDto = new ContentDto()
					.setArticleId(articleDto.getId())
					.setAuthorId(articleDto.getAuthorId())
					.setText(postFormCommand.getText());
			
			if(userDto.getId()==articleDto.getAuthorId()) {
				List<ArticleImageDto> listArticleImageDto = new ArrayList<>();
				
				for(int i=0;i<images.size();i++) {
					listArticleImageDto.add(new ArticleImageDto());
					listArticleImageDto.get(i).setArticleId(articleDto.getId());
				}
				
				articleService.updateArticle(articleDto);
				articleService.updateText(contentDto);
				articleService.delArticleImage(articleDto.getId());
				articleService.addArticleImage(listArticleImageDto);
				
				int count=0;	
				
				for(MultipartFile file : images) {
					File image = new File(imgPath+listArticleImageDto.get(count).getId()+".png");
					file.transferTo(image);
					count++;
				}
				
				return Response.ok().setMetadata("/article/"+articleDto.getId());
			}
			
		} catch(Exception exception) {	
		}
			return Response.unauthorized();
	}	
		
}