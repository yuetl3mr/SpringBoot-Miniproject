package com.employee.management.controller;

import com.employee.management.dto.DepartmentStatistics;
import com.employee.management.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees/by-department")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<DepartmentStatistics>> getEmployeeCountByDepartment() {
        List<DepartmentStatistics> statistics = employeeService.getEmployeeCountByDepartment();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/employees/total")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Long>> getTotalEmployeeCount() {
        Long total = employeeService.getTotalEmployees();
        Map<String, Long> response = new HashMap<>();
        response.put("totalEmployees", total);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllStatistics() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalEmployees", employeeService.getTotalEmployees());
        response.put("employeesByDepartment", employeeService.getEmployeeCountByDepartment());
        return ResponseEntity.ok(response);
    }
}

