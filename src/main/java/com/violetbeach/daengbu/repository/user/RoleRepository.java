package com.violetbeach.daengbu.repository.user;

import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository {
	
	String findById(Long id);
	
}