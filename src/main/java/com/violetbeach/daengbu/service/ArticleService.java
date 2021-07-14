package com.violetbeach.daengbu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.violetbeach.daengbu.dto.model.article.ArticleDto;
import com.violetbeach.daengbu.dto.model.article.ArticleImageDto;
import com.violetbeach.daengbu.dto.model.article.ContentDto;
import com.violetbeach.daengbu.dto.model.article.KindDto;
import com.violetbeach.daengbu.dto.model.article.WishlistDto;
import com.violetbeach.daengbu.repository.article.ArticleRepository;
import com.violetbeach.daengbu.repository.article.KindRepository;

@Service
public class ArticleService {

	@Autowired
	ArticleRepository articleRepository;
	
	@Autowired
	KindRepository kindRepository;
	
	public List<KindDto> getKinds() {
		return kindRepository.getAll();
	}
	
	public String getKindById(Long id) {
		return kindRepository.getValueById(id);
	}
	
	public Long post(ArticleDto articleDto, ContentDto contentDto, List<ArticleImageDto> listImageDto) {
		articleRepository.postArticle(articleDto);
		contentDto.setArticleId(articleDto.getId());
		articleRepository.postText(contentDto);
		for(ArticleImageDto imageDto : listImageDto) {
			imageDto.setArticleId(articleDto.getId());
			articleRepository.postImage(imageDto);
		}
		return articleDto.getId();
	}
	
	public List<ArticleDto> getArticleList(ArticleDto articleDto){
		return articleRepository.getArticleList(articleDto);
	}
	
	public List<ArticleImageDto> getArticleImageList(ArticleDto articleDto){
		return articleRepository.getArticleImageList(articleDto);
	}
	
	public ArticleDto getById(Long id) {
		return articleRepository.getById(id);
	}
	
	public String getContentByArticleId(Long id) {
		return articleRepository.getContentByArticleId(id);
	}
	
	public List<Long> getArticleImageByArticleId(Long id) {
		return articleRepository.getArticleImageByArticleId(id);
	}
	
	public void articleViewCount(Long id) {
		articleRepository.articleViewCount(id);
	}
	
	public void addWishlist(WishlistDto wishlistDto) {
		articleRepository.addWishlist(wishlistDto);
	}
	
	public void delWishlist(WishlistDto wishlistDto) {
		articleRepository.delWishlist(wishlistDto);
	}
	
	public boolean getWishCountById(WishlistDto wishlistDto) {
		if(articleRepository.getWishCountById(wishlistDto)==0) return false;
    	else return true;
	}

	public List<ArticleDto> getWishArticleList(Long id) {
		return articleRepository.getWishArticleList(id);
	}
	
	public List<ArticleImageDto> getWishArticleImageList(Long id) {
		return articleRepository.getWishArticleImageList(id);
	}
	
}