package com.notes_sharing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notes_sharing.entity.UserDtls;

public interface UserRepository extends JpaRepository<UserDtls,Integer>{
	
	public UserDtls findByEmail(String email);
	

} 