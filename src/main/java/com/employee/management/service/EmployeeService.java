package com.employee.management.service;

import com.employee.management.entity.Department;
import com.employee.management.entity.Employee;
import com.employee.management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, DepartmentService departmentService) {
        this.employeeRepository = employeeRepository;
        this.departmentService = departmentService;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        
        employee.setName(employeeDetails.getName());
        employee.setEmail(employeeDetails.getEmail());
        if (employeeDetails.getDepartment() != null) {
            employee.setDepartment(employeeDetails.getDepartment());
        }
        
        return employeeRepository.save(employee);
    }

    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Employee> searchEmployeesByDepartment(String departmentName) {
        return employeeRepository.findByDepartmentNameContaining(departmentName);
    }

    public List<Employee> searchEmployees(String name, String departmentName) {
        if (name != null && !name.isEmpty() && departmentName != null && !departmentName.isEmpty()) {
            return employeeRepository.findByNameOrDepartmentName(name, departmentName);
        } else if (name != null && !name.isEmpty()) {
            return employeeRepository.findByNameContainingIgnoreCase(name);
        } else if (departmentName != null && !departmentName.isEmpty()) {
            return employeeRepository.findByDepartmentNameContaining(departmentName);
        }
        return getAllEmployees();
    }
}
