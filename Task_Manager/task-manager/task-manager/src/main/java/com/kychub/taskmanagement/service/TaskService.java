package com.kychub.taskmanagement.service;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kychub.taskmanagement.model.Task;
import com.kychub.taskmanagement.model.User;
import com.kychub.taskmanagement.repository.TaskRepository;
import com.kychub.taskmanagement.repository.UserRepository;



@Service
public class TaskService {
	
	@Autowired
	private TaskRepository taskRepository;
	
	 @Autowired
	    private UserRepository userRepository;
	 
	 
	
	  public Task updateTaskForUser(int taskId, int userId, Task updatedTaskDetails) {
	        Optional<User> userOptional = userRepository.findById(userId);
	        Optional<Task> taskOptional = taskRepository.findById(taskId);

	        if (taskOptional.isPresent() && userOptional.isPresent()) {
	            Task existingTask = taskOptional.get();
	            User user = userOptional.get();

	             
	            if (!(existingTask.getUser().getId() == user.getId())) {
	                throw new IllegalArgumentException("Task not found for the given user");
	            }

	             
	            existingTask.setTitle(updatedTaskDetails.getTitle());
	            existingTask.setDescription(updatedTaskDetails.getDescription());
	            existingTask.setPriority(updatedTaskDetails.getPriority());
	            existingTask.setDueDate(updatedTaskDetails.getDueDate());
	            existingTask.setStatus(updatedTaskDetails.getStatus());
	            existingTask.setCompletedAt(updatedTaskDetails.getCompletedAt());

	           
	            return taskRepository.save(existingTask);
	        } else {
	            throw new IllegalArgumentException("Task or User not found");
	        }
	    }
	  
	 
	    public Page<Task> getTasksByUserWithFilters(Integer userId, String status, String priority, Pageable pageable) {
	        return taskRepository.findByUserWithFilters(userId, status, priority, pageable) ;
	    }
	 
	
	public Task createTask(Task task)
	{
		return taskRepository.save(task);
	}
	
	 
	public Page<Task> listTasksByUser(User user, Pageable pageable)
	{
		return taskRepository.findByUser(user, pageable);
	}
	
	public Task markTaskAsCompleted(Integer taskId)
	{
		Task task=taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found Exception"));
		task.setStatus("completed");
		return taskRepository.save(task);
	}
	
	public void deleteTaskForUser(Integer userId, Integer taskId) {
	    
	    Task task = taskRepository.findById(taskId)
	            .orElseThrow(() -> new RuntimeException("Task not found"));

	    
	    if (!(task.getUser().getId() == (userId))) {
	        throw new RuntimeException("Task does not belong to the specified user");
	    }

	    
	    taskRepository.delete(task);
	}


}
