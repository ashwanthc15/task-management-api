package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.TaskExecution;
import com.example.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private CommandValidationService commandValidationService;
    
    @Autowired
    private TaskExecutionService taskExecutionService;
    
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    
    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }
    
    public List<Task> findTasksByName(String name) {
        return taskRepository.findByNameContainingIgnoreCase(name);
    }
    
    public Task createOrUpdateTask(Task task) throws IllegalArgumentException {
        if (!commandValidationService.isCommandSafe(task.getCommand())) {
            throw new IllegalArgumentException("Command contains unsafe or malicious code");
        }
        return taskRepository.save(task);
    }
    
    public boolean deleteTask(String id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Optional<Task> executeTask(String taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            TaskExecution execution = taskExecutionService.executeCommand(task.getCommand());
            task.getTaskExecutions().add(execution);
            return Optional.of(taskRepository.save(task));
        }
        return Optional.empty();
    }
}