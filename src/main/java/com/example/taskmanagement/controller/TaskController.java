package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Validated
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @GetMapping("/tasks")
    public ResponseEntity<Object> getTasks(@RequestParam(required = false) String id) {
        if (id != null) {
            Optional<Task> task = taskService.getTaskById(id);
            if (task.isPresent()) {
                return ResponseEntity.ok(task.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            List<Task> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        }
    }
    
    @PutMapping("/tasks")
    public ResponseEntity<Object> createOrUpdateTask(@Valid @RequestBody Task task) {
        try {
            Task savedTask = taskService.createOrUpdateTask(task);
            return ResponseEntity.ok(savedTask);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @DeleteMapping("/tasks")
    public ResponseEntity<Object> deleteTask(@RequestParam String id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.ok().body("{\"message\": \"Task deleted successfully\"}");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/tasks/search")
    public ResponseEntity<Object> findTasksByName(@RequestParam String name) {
        List<Task> tasks = taskService.findTasksByName(name);
        if (tasks.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tasks);
    }
    
    @PutMapping("/tasks/{taskId}/execute")
    public ResponseEntity<Object> executeTask(@PathVariable String taskId) {
        Optional<Task> task = taskService.executeTask(taskId);
        if (task.isPresent()) {
            return ResponseEntity.ok(task.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}