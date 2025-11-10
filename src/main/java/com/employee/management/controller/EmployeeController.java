package com.employee.management.controller;

import com.employee.management.dto.EmployeeRequest;
import com.employee.management.entity.Department;
import com.employee.management.entity.Employee;
import com.employee.management.exception.ResourceNotFoundException;
import com.employee.management.service.DepartmentService;
import com.employee.management.service.EmployeeService;
import com.employee.management.service.UtilityService;
import jakarta.validation.Valid;
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
        Employee employee = employeeService.getEmployeeByIdOrThrow(id);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@Valid @RequestBody EmployeeRequest request) {
        Department department = departmentService.getDepartmentById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));
        
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(department);
        
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        Department department = departmentService.getDepartmentById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + request.getDepartmentId()));
        
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(department);
        
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeOrThrow(id);
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

    @GetMapping("/count")
    public ResponseEntity<EmployeeCountResponse> getEmployeeCount() {
        Long count = employeeService.getTotalEmployeeCount();
        return ResponseEntity.ok(new EmployeeCountResponse(count));
    }

    static class EmployeeCountResponse {
        private Long totalEmployees;

        public EmployeeCountResponse(Long totalEmployees) {
            this.totalEmployees = totalEmployees;
        }

        public Long getTotalEmployees() {
            return totalEmployees;
        }

        public void setTotalEmployees(Long totalEmployees) {
            this.totalEmployees = totalEmployees;
        }
    }
}
