package com.employee.management.controller;

import com.employee.management.entity.Department;
import com.employee.management.entity.Employee;
import com.employee.management.service.DepartmentService;
import com.employee.management.service.EmployeeService;
import com.employee.management.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final UtilityService utilityService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService, UtilityService utilityService, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.utilityService = utilityService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody EmployeeRequest request) {
        Optional<Department> department = departmentService.getDepartmentById(request.getDepartmentId());
        if (department.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(department.get());
        
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest request) {
        Optional<Department> department = departmentService.getDepartmentById(request.getDepartmentId());
        if (department.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(department.get());
        
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            return ResponseEntity.ok(updatedEmployee);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department) {
        List<Employee> employees = employeeService.searchEmployees(name, department);
        return ResponseEntity.ok(employees);
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

    static class EmployeeRequest {
        private String name;
        private String email;
        private Long departmentId;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Long getDepartmentId() {
            return departmentId;
        }

        public void setDepartmentId(Long departmentId) {
            this.departmentId = departmentId;
        }
    }
}
