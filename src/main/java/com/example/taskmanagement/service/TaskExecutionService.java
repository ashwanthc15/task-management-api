package com.example.taskmanagement.service;

import com.example.taskmanagement.model.TaskExecution;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class TaskExecutionService {
    
    public TaskExecution executeCommand(String command) {
        Date startTime = new Date();
        StringBuilder output = new StringBuilder();
        
        try {
            // For demonstration, we'll simulate command execution
            // In a real Kubernetes environment, this would interact with K8s API
            ProcessBuilder pb = new ProcessBuilder();
            
            // Set up command based on OS
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                pb.command("cmd", "/c", command);
            } else {
                pb.command("sh", "-c", command);
            }
            
            Process process = pb.start();
            
            // Read output
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            // Read error output
            try (BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    output.append("ERROR: ").append(line).append("\n");
                }
            }
            
            // Wait for process to complete (max 30 seconds)
            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                output.append("Command execution timed out after 30 seconds");
            }
            
        } catch (IOException | InterruptedException e) {
            output.append("Error executing command: ").append(e.getMessage());
        }
        
        Date endTime = new Date();
        return new TaskExecution(startTime, endTime, output.toString().trim());
    }
}