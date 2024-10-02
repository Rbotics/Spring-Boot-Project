package com.kychub.taskmanagement.task_manager.service;

 import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.kychub.taskmanagement.model.User;
import com.kychub.taskmanagement.repository.UserRepository;
import com.kychub.taskmanagement.service.UserService;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @SuppressWarnings("deprecation")
	@BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

       
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    void testGetAllUsers() {
      
        PageRequest pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(Collections.singletonList(user));

        
        when(userRepository.findAll(pageable)).thenReturn(page);

       
        Page<User> result = userService.getAllUsers(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(user.getUsername(), result.getContent().get(0).getUsername());

        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void testSearchUsers() {
       
        PageRequest pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(Collections.singletonList(user));

        
        when(userRepository.findByUsernameContainingIgnoreCase(anyString(), any(PageRequest.class))).thenReturn(page);

       
        Page<User> result = userService.searchUsers("test", pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("testuser", result.getContent().get(0).getUsername());

        verify(userRepository, times(1)).findByUsernameContainingIgnoreCase("test", pageable);
    }

    @Test
    void testRegisterUser() {
        
        when(userRepository.save(any(User.class))).thenReturn(user);

       
        User savedUser = userService.registerUser(user);

       
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("password", savedUser.getPassword()));
        assertEquals(user.getUsername(), savedUser.getUsername());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        
        userService.deleteUser(1);

       
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testListUsers() {
       
        PageRequest pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(Collections.singletonList(user));

       
        when(userRepository.findAll(pageable)).thenReturn(page);

        
        Page<User> result = userService.listUsers(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(user.getUsername(), result.getContent().get(0).getUsername());

        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void testFindById() {
       
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        
        Optional<User> result = userService.findById(1);
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());

        verify(userRepository, times(1)).findById(1);
    }
}
