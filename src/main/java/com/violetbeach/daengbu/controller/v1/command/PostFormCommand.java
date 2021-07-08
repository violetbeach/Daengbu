package com.violetbeach.daengbu.controller.v1.command;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PostFormCommand {
	
	private Long authorId;
	private Long kindId;
	private String location1;
	private String location2;
	private String age;
	private String gender;
	private String title;
	private Long articleId;
	private String text;
	private List<MultipartFile> images;

}