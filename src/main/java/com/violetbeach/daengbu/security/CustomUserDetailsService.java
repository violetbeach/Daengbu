package com.violetbeach.daengbu.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.violetbeach.daengbu.dto.model.user.UserDto;
import com.violetbeach.daengbu.repository.user.RoleRepository;
import com.violetbeach.daengbu.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserService userService;
	
	private final RoleRepository roleRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserDto userDto = userService.findByEmail(email);
		if( userDto == null ) {
			throw new UsernameNotFoundException(email+"은 존재하지 않는 이메일입니다.");
		} else {
			List<GrantedAuthority> authorities = getUserAuthority(userDto);
			return buildUserForAuthentication(userDto, authorities);
		}
	}
	
	private List<GrantedAuthority> getUserAuthority(UserDto userDto) {
        Set<GrantedAuthority> role = new HashSet<>();
        	role.add(new SimpleGrantedAuthority(roleRepository.findById(userDto.getRole())));
        return new ArrayList<GrantedAuthority>(role);
    }
	
	private UserDetails buildUserForAuthentication(UserDto user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
	
}