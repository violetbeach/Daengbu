package com.violetbeach.daengbu.repository.article;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.violetbeach.daengbu.dto.model.article.ArticleDto;
import com.violetbeach.daengbu.dto.model.article.ArticleImageDto;
import com.violetbeach.daengbu.dto.model.article.ContentDto;
import com.violetbeach.daengbu.dto.model.article.WishlistDto;

@Repository
public interface ArticleRepository {
	
	Long postArticle(ArticleDto articleDto);
	Long postText(ContentDto contentDto);
	Long postImage(ArticleImageDto imageDto);
	List<ArticleDto> getArticleList(ArticleDto articleSearchDto);
	List<ArticleImageDto> getArticleImageList(ArticleDto articleSearchDto);
	ArticleDto getById(Long id);
	String getContentByArticleId(Long id);
	List<Long> getArticleImageByArticleId(Long id);
	void articleViewCount(Long id);
	void addWishlist(WishlistDto wishlistDto);
	int getWishCountById(WishlistDto wishListDto);
	void delWishlist(WishlistDto wishlistDto);
	List<ArticleDto> getWishArticleList(Long id);
	List<ArticleImageDto> getWishArticleImageList(Long id);
	void delArticle(Long id);
	void delArticleImage(Long id);
	void updateArticle(ArticleDto articleDto);
	void updateText(ContentDto contentDto);
}