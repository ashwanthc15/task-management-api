package com.example.taskmanagement.service;

import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class CommandValidationService {
    
    private static final List<String> DANGEROUS_COMMANDS = Arrays.asList(
        "rm", "del", "format", "fdisk", "mkfs", "dd", "shutdown", "reboot",
        "halt", "poweroff", "init", "kill", "killall", "pkill", "sudo",
        "su", "chmod", "chown", "passwd", "useradd", "userdel", "usermod",
        "crontab", "at", "wget", "curl", "nc", "netcat", "telnet", "ssh",
        "scp", "rsync", "mount", "umount", "iptables", "ufw", "firewall-cmd"
    );
    
    private static final List<String> DANGEROUS_PATTERNS = Arrays.asList(
        "&&", "||", ";", "|", ">", ">>", "<", "$(", "`", "eval", "exec"
    );
    
    public boolean isCommandSafe(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }
        
        String lowerCommand = command.toLowerCase().trim();
        
        // Check for dangerous commands
        for (String dangerous : DANGEROUS_COMMANDS) {
            if (lowerCommand.startsWith(dangerous + " ") || 
                lowerCommand.equals(dangerous)) {
                return false;
            }
        }
        
        // Check for dangerous patterns
        for (String pattern : DANGEROUS_PATTERNS) {
            if (command.contains(pattern)) {
                return false;
            }
        }
        
        return true;
    }
}
