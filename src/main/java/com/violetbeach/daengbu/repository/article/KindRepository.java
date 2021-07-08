package com.violetbeach.daengbu.repository.article;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.violetbeach.daengbu.dto.model.article.KindDto;

@Repository
public interface KindRepository {
	
	List<KindDto> getAll();
	String getValueById(Long id);
	
}