package com.violetbeach.daengbu.repository.user;

import org.springframework.stereotype.Repository;

import com.violetbeach.daengbu.dto.model.user.UserDto;

@Repository
public interface UserRepository {
	
	UserDto findByEmail(String email);
	int getCountByEmail(String email);
	int getCountByUsername(String userName);
	void regist(UserDto userDto);
	String getUsernameById(Long id);
	void addMailAuth(String email, String auth);
	String getMailAuthByEmail(String email);
}