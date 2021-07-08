package com.violetbeach.daengbu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.violetbeach.daengbu.dto.model.article.ArticleDto;
import com.violetbeach.daengbu.dto.model.article.ArticleImageDto;
import com.violetbeach.daengbu.dto.model.article.ContentDto;
import com.violetbeach.daengbu.dto.model.article.KindDto;
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
	
}