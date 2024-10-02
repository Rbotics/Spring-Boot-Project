package com.kychub.taskmanagement.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kychub.taskmanagement.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
	  

	@SuppressWarnings("unchecked")
	Page findAll(Pageable pageable);

	Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
	
	  Optional<User> findByEmail(String email);
	    
	    User findByUsername(String username);
	    
	   
}
