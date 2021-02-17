package com.violetbeach.daengbu.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.violetbeach.daengbu.dto.UserDto;

@Repository
public interface UserRepository {
	
	List<UserDto> findAllUsers();
	
	UserDto findUserById(int id);
	
	int signUp(UserDto userDto); 
}
