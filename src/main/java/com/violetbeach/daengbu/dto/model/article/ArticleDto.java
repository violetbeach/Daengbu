package com.violetbeach.daengbu.dto.model.article;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArticleDto{
	
	private Long id;
	private Long authorId;
	private Long kindId;
	private String location1;
	private String location2;
	private String age;
	private String gender;
	private String title;
	private int viewCount;
	public String createdDatetime;
	private Long repImg;
	private String kindValue;
	
}