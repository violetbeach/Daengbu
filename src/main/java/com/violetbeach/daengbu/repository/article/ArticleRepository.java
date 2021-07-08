package com.violetbeach.daengbu.repository.article;

import org.springframework.stereotype.Repository;

import com.violetbeach.daengbu.dto.model.article.ArticleDto;
import com.violetbeach.daengbu.dto.model.article.ArticleImageDto;
import com.violetbeach.daengbu.dto.model.article.ContentDto;

@Repository
public interface ArticleRepository {
	
	Long postArticle(ArticleDto articleDto);
	Long postText(ContentDto contentDto);
	Long postImage(ArticleImageDto imageDto);
	
}