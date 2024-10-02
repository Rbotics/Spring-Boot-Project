package com.kychub.taskmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kychub.taskmanagement.model.User;
import com.kychub.taskmanagement.repository.UserRepository;
import com.kychub.taskmanagement.service.UserService;

@RestController
@RequestMapping("")
public class UserController 
{
	@Autowired
	private UserService userService;
	
	 private final UserRepository userRepository;
	 
	 @PostMapping("/register")
		public String registerUser(@RequestBody User user)
		{
		 
			userService.registerUser(user);
			return "User has registered Succesfully ";
		}
	 
	 
  
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTask(@PathVariable Integer id)
	{
		userService.deleteUser(id);
		return ResponseEntity.ok("User deleted Successfully"); 
	}
	
	public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;}
	
	 @GetMapping("/all")
	    public Page<User> getAllUsers(
	            @RequestParam(defaultValue = "0") int page,          
	            @RequestParam(defaultValue = "10") int size,         
	            @RequestParam(defaultValue = "username") String sortBy  
	    ) {
	        
	        return userService.getAllUsers(PageRequest.of(page, size, Sort.by(sortBy)));
	    }
	
	 
	 @GetMapping("/search")
	    public Page<User> searchUsers(
	            @RequestParam String username,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size,
	            @RequestParam(defaultValue = "username") String sortBy
	    ) {
	        
	        return userService.searchUsers(username, PageRequest.of(page, size, Sort.by(sortBy)));
	    }
	 
 

}
