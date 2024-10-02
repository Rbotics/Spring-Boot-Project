package com.kychub.taskmanagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kychub.taskmanagement.model.User;
import com.kychub.taskmanagement.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@SuppressWarnings("unchecked")
	public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
	
	 public Page<User> searchUsers(String username, Pageable pageable) {
	        return userRepository.findByUsernameContainingIgnoreCase(username, pageable);
	    }

	
	public User registerUser(User user)
	{
		  user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
	        return userRepository.save(user);
	}
	
	public void deleteUser(Integer id)
	{
		userRepository.deleteById(id);
	}
	
	 @SuppressWarnings("unchecked")
	public Page<User> listUsers(Pageable pageable) {
	        return userRepository.findAll(pageable);
	    }
	 
	
	 public Optional<User> findById(Integer id) {
	        return userRepository.findById(id); 
	 }

	 public User findUserByUsername(String username) {
	        return userRepository.findByUsername(username);
	        
	        
}
	 
	  

	    
}
