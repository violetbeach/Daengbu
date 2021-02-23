package com.violetbeach.daengbu.repository.user;

import org.springframework.stereotype.Repository;

import com.violetbeach.daengbu.dto.model.user.UserDto;

@Repository
public interface UserRepository {
	
	UserDto findUserByEmail(String email);
	
}