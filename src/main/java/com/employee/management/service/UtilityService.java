package com.employee.management.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UtilityService {

    public String formatString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        return input.trim();
    }

    public String generateEmployeeCode(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "EMP" + System.currentTimeMillis();
        }
        String cleaned = name.toUpperCase().replaceAll("[^A-Z0-9]", "");
        String prefix = cleaned.length() >= 3 ? cleaned.substring(0, 3) : cleaned;
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(7);
        return prefix + timestamp;
    }

    public String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}

