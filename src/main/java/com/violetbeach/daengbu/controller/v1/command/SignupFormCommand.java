package com.violetbeach.daengbu.controller.v1.command;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SignupFormCommand {
	
	@NotBlank
    @Email
    private String email;
	
	@NotBlank
    @Size(min = 2, max = 16)
    private String username;

    @NotBlank
    @Size(min = 8, max = 16)
    private String password;

    @NotBlank
    @Size(min = 5, max = 13)
    private String tel;

}
