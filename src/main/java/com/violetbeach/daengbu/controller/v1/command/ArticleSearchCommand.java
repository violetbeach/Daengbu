package com.violetbeach.daengbu.controller.v1.command;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ArticleSearchCommand {
	
	private Long kindId;
	private String location1;
	private String age;
	private String gender;
	private String authorId;

}