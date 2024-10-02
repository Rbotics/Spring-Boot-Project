package com.kychub.taskmanagement.task_manager.service;

 

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.springframework.data.domain.Pageable;

import com.kychub.taskmanagement.model.Task;
import com.kychub.taskmanagement.model.User;
import com.kychub.taskmanagement.repository.TaskRepository;
import com.kychub.taskmanagement.repository.UserRepository;
import com.kychub.taskmanagement.service.TaskService;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        
        user = new User();
        user.setId(1);
        user.setUsername("testuser");

        task = new Task();
        task.setId(1);
        task.setTitle("Test Task");
        task.setUser(user);
    }

    @Test
    void testUpdateTaskForUser_Success() {
        
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        
        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("Updated Title");
        updatedTaskDetails.setDescription("Updated Description");

       
        Task updatedTask = taskService.updateTaskForUser(1, 1, updatedTaskDetails);

        
        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());

        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testUpdateTaskForUser_TaskNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(taskRepository.findById(anyInt())).thenReturn(Optional.empty());

        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("Updated Title");

        
        assertThrows(IllegalArgumentException.class, () -> {
            taskService.updateTaskForUser(1, 1, updatedTaskDetails);
        });
    }

    @Test
    void testGetTasksByUserWithFilters() {
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(Collections.singletonList(task));

        
        when(taskRepository.findByUserWithFilters(anyInt(), anyString(), anyString(), any(Pageable.class)))
                .thenReturn(page);

        
        Page<Task> result = taskService.getTasksByUserWithFilters(1, "OPEN", "HIGH", pageable);

        
        assertEquals(1, result.getTotalElements());
        assertEquals(task.getTitle(), result.getContent().get(0).getTitle());
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask = taskService.createTask(task);

        assertEquals(task.getTitle(), createdTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

   

    @Test
    void testListTasksByUser() {
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(Collections.singletonList(task));

        when(taskRepository.findByUser(any(User.class), any(Pageable.class))).thenReturn(page);

       
        Page<Task> result = taskService.listTasksByUser(user, pageable);

        
        assertEquals(1, result.getTotalElements());
        assertEquals(task.getTitle(), result.getContent().get(0).getTitle());
    }

    @Test
    void testMarkTaskAsCompleted() {
       
        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        
        Task completedTask = taskService.markTaskAsCompleted(1);

        
        assertEquals("completed", completedTask.getStatus());
        verify(taskRepository, times(1)).save(task);
    }
}
