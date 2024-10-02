package com.kychub.taskmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kychub.taskmanagement.model.Task;
import com.kychub.taskmanagement.model.User;
import com.kychub.taskmanagement.repository.UserRepository;
import com.kychub.taskmanagement.service.TaskService;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/taskcreate")
    public ResponseEntity<String> createTask(@RequestBody Task task) {
 
    	User user=userRepository.findById(task.getUser().getId()) .orElseThrow(() -> new RuntimeException("Address not found"));
    	task.setUser(user);
    	userRepository.save(user);
        taskService.createTask(task);
        return ResponseEntity.ok("Task created successfully");
    }
    
   @PatchMapping("/task/update")
    public ResponseEntity<Task> updateTask(
            @RequestBody Task updatedTaskDetails) {
    	
    	int taskId = updatedTaskDetails.getId();
    	int userId = updatedTaskDetails.getUser().getId();
 
         
        Task updatedTask = taskService.updateTaskForUser(taskId, userId, updatedTaskDetails);

        return ResponseEntity.ok(updatedTask);
    }
    
    @DeleteMapping("/user/{userId}/task/{taskId}")
    public ResponseEntity<String> deleteTask(
        @PathVariable("userId") Integer userId, 
        @PathVariable("taskId") Integer taskId) {

        taskService.deleteTaskForUser(userId, taskId);
        return ResponseEntity.ok("Task deleted successfully");
    }

   @GetMapping("/fetchTasks")
    public ResponseEntity<Page<Task>> listTasks(@RequestParam int page, @RequestParam int size) {
       
        User currentUser = getCurrentAuthenticatedUser();
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(taskService.listTasksByUser(currentUser, pageable));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<String> markTaskAsCompleted(@PathVariable Integer id) {
        taskService.markTaskAsCompleted(id);
        return ResponseEntity.ok("Task marked as completed");
    }
    
    

    private User getCurrentAuthenticatedUser() {
       
        return new User();  
    }
    
  
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Task>> getUserTasks(
            @PathVariable("userId") Integer userId,
            @RequestParam(value = "status", required = false) String status,   
            @RequestParam(value = "priority", required = false) String priority,  
            @RequestParam(value = "page", defaultValue = "0") int page,  
            @RequestParam(value = "size", defaultValue = "10") int size,   
            @RequestParam(value = "sortBy", defaultValue = "dueDate") String sortBy,   
            @RequestParam(value = "sortDir", defaultValue = "ASC") String sortDir  
    ) {

         

    	  PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));

        
        Page<Task> tasks = taskService.getTasksByUserWithFilters(userId, status, priority, pageable);

        return ResponseEntity.ok(tasks);
    }
}

