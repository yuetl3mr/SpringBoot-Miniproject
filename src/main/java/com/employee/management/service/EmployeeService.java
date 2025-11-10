package com.employee.management.service;

import com.employee.management.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeService {

    private final List<Employee> employees = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public EmployeeService() {
        employees.add(new Employee(counter.incrementAndGet(), "Nguyen Van A", "nguyenvana@example.com", "IT"));
        employees.add(new Employee(counter.incrementAndGet(), "Tran Thi B", "tranthib@example.com", "HR"));
        employees.add(new Employee(counter.incrementAndGet(), "Le Van C", "levanc@example.com", "Finance"));
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public Employee addEmployee(Employee employee) {
        employee.setId(counter.incrementAndGet());
        employee.setCreatedAt(java.time.LocalDateTime.now());
        employees.add(employee);
        return employee;
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employees.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst();
    }
}

