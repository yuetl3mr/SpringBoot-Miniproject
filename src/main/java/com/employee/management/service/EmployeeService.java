package com.employee.management.service;

import com.employee.management.entity.Department;
import com.employee.management.entity.Employee;
import com.employee.management.exception.ResourceNotFoundException;
import com.employee.management.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

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
        logger.info("Adding new employee: name={}, email={}, department={}", 
                employee.getName(), employee.getEmail(), employee.getDepartment().getName());
        Employee saved = employeeRepository.save(employee);
        logger.info("Employee added successfully with ID: {}", saved.getId());
        return saved;
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }


    public Employee getEmployeeByIdOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        logger.info("Updating employee with ID: {}", id);
        Employee employee = getEmployeeByIdOrThrow(id);
        
        String oldName = employee.getName();
        String oldEmail = employee.getEmail();
        
        employee.setName(employeeDetails.getName());
        employee.setEmail(employeeDetails.getEmail());
        if (employeeDetails.getDepartment() != null) {
            employee.setDepartment(employeeDetails.getDepartment());
        }
        
        Employee updated = employeeRepository.save(employee);
        logger.info("Employee updated: ID={}, oldName={}, newName={}, oldEmail={}, newEmail={}", 
                id, oldName, updated.getName(), oldEmail, updated.getEmail());
        return updated;
    }

    public void deleteEmployeeOrThrow(Long id) {
        logger.info("Deleting employee with ID: {}", id);
        if (!employeeRepository.existsById(id)) {
            logger.warn("Attempted to delete employee with ID {} but not found", id);
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
        logger.info("Employee deleted successfully with ID: {}", id);
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
