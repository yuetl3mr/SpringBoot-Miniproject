package com.employee.management.controller;

import com.employee.management.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final UtilityService utilityService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeController(UtilityService utilityService, PasswordEncoder passwordEncoder) {
        this.utilityService = utilityService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/test-utility")
    public String testUtility(@RequestParam String name) {
        String employeeCode = utilityService.generateEmployeeCode(name);
        String formattedName = utilityService.formatString(name);
        return "Employee Code: " + employeeCode + ", Formatted Name: " + formattedName;
    }

    @GetMapping("/test-encoder")
    public String testEncoder(@RequestParam String password) {
        String encoded = passwordEncoder.encode(password);
        return "Encoded Password: " + encoded;
    }
}

