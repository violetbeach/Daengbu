package com.violetbeach.daengbu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.repository.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    
    public boolean isDuplicateEmail(String email) {
    	if(userRepository.getCountByEmail(email)==0) return false;
    	else return true;
    }
    
    public boolean isDuplicateUsername(String username) {
    	if(userRepository.getCountByUsername(username)==0) return false;
    	else return true;
    }
    
    public void regist(UserDto userDto) {
    	if(userRepository.getCountByEmail(userDto.getEmail())==0) {
    		userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
    		log.info("회원가입, email: '{}'", userDto.getEmail());
        	userRepository.regist(userDto);
    	} else {
    		log.info("회원가입 실패");
    	}
    }

}
