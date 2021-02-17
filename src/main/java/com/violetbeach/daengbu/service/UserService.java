package com.violetbeach.daengbu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.violetbeach.daengbu.dto.UserDto;
import com.violetbeach.daengbu.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public List<UserDto> findAllUsers(){
		return repository.findAllUsers();
	}
	
	public UserDto findUserById(int id){
		return repository.findUserById(id);
	}
	
	public int signUp(UserDto userDto) {
		repository.signUp(userDto);
		return userDto.getId();
	}
	
}
