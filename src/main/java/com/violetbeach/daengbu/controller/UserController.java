package com.violetbeach.daengbu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.violetbeach.daengbu.dto.UserDto;
import com.violetbeach.daengbu.service.UserService;

@RestController
@RequestMapping(value ="user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public List<UserDto> findAllUsers() {
		return userService.findAllUsers();
	}
	
	@GetMapping("/{id}")
	public UserDto findUserById(@PathVariable int id) {
		return userService.findUserById(id);
	}
	
	@GetMapping("/signup")
	public int signUp(UserDto userDto) {
		return userService.signUp(userDto);
	}
	
}
