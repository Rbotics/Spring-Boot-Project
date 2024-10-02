package com.kychub.taskmanagement.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kychub.taskmanagement.model.Task;
import com.kychub.taskmanagement.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
	PageRequest pageable = PageRequest.of(0, 10, Sort.by("dueDate").ascending());
	Page<Task> findByUser(User user, Pageable pageable);
	
	   @Query("SELECT t FROM Task t WHERE t.user.id = :userId "
	         + "AND (:status IS NULL OR t.status = :status) "
	         + "AND (:priority IS NULL OR t.priority = :priority)")
	    Page<Task> findByUserWithFilters(
	            Integer userId,
	            String status,
	            String priority,
	            Pageable pageable
	    );
	 
}
