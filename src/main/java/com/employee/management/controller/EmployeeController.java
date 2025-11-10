package com.employee.management.controller;

import com.employee.management.model.Employee;
import com.employee.management.service.EmployeeService;
import com.employee.management.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final UtilityService utilityService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeController(EmployeeService employeeService, UtilityService utilityService, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.utilityService = utilityService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.addEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
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

