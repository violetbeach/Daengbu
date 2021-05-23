package com.violetbeach.daengbu.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.exception.CustomException;
import com.violetbeach.daengbu.exception.DtoType;
import com.violetbeach.daengbu.exception.ExceptionType;
import com.violetbeach.daengbu.repository.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public UserDto findByEmail(String email) {
        Optional<UserDto> optUserDto = Optional.ofNullable(userRepository.findByEmail(email));
        if(optUserDto.isPresent()){
        	UserDto userDto = optUserDto.get();
        	return userDto;
        }
        throw exception(DtoType.USER, ExceptionType.DTO_NOT_FOUND, email);
    }
    
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
    		throw exception(DtoType.USER, ExceptionType.DUPLICATE_DTO, userDto.getEmail());
    	}
    }

    private RuntimeException exception(DtoType dtoType, ExceptionType exceptionType, String... args) {
        return CustomException.throwException(dtoType, exceptionType, args);
    }	
    
}
