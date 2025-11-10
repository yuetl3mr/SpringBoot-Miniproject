package com.employee.management.config;

import com.employee.management.entity.Department;
import com.employee.management.entity.Employee;
import com.employee.management.repository.DepartmentRepository;
import com.employee.management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            Department it = new Department("IT");
            Department hr = new Department("HR");
            Department finance = new Department("Finance");
            
            departmentRepository.save(it);
            departmentRepository.save(hr);
            departmentRepository.save(finance);
            
            if (employeeRepository.count() == 0) {
                employeeRepository.save(new Employee("Nguyen Van A", "nguyenvana@example.com", it));
                employeeRepository.save(new Employee("Tran Thi B", "tranthib@example.com", hr));
                employeeRepository.save(new Employee("Le Van C", "levanc@example.com", finance));
            }
        }
    }
}

