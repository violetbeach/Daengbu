package com.violetbeach.daengbu.controller.v1.command;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChangePasswordCommand {
	
	private String newPassword;
	private String oldPassword;

}