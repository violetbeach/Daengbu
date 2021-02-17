package com.violetbeach.daengbu.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserDto{
	
	private int id;
	private String email;
	@JsonIgnore
	private String password;
	private String username;
	private String tel;
	private int location1;
	private int location2;
	private LocalDateTime register_datetime;
	private String register_ip;
	private LocalDateTime last_login_datetime;
	private String last_login_ip;
	private String role;
	private int account_locked;
	private LocalDateTime date_withdraw;
	
}
