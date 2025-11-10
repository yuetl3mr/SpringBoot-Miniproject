package com.employee.management.config;

import com.employee.management.entity.Department;
import com.employee.management.entity.Employee;
import com.employee.management.entity.User;
import com.employee.management.repository.DepartmentRepository;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        if (userRepository.count() == 0) {
            Set<User.Role> adminRoles = new HashSet<>();
            adminRoles.add(User.Role.ADMIN);
            User admin = new User("admin", passwordEncoder.encode("admin123"), adminRoles);
            userRepository.save(admin);

            Set<User.Role> userRoles = new HashSet<>();
            userRoles.add(User.Role.USER);
            User user = new User("user", passwordEncoder.encode("user123"), userRoles);
            userRepository.save(user);
        }
    }
}
